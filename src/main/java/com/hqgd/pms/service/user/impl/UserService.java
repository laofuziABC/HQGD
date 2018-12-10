package com.hqgd.pms.service.user.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.ExcelFormat;
import com.hqgd.pms.dao.user.UserMapper;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述： 作者：姚绒 日期：2018年11月5日 上午9:43:08
 *
 */
@Slf4j
@Service
public class UserService implements IUserService {

	@Resource
	private UserMapper userMapper;

	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public User findUserByUserName(String userName) {
		return userMapper.findUserByUserName(userName);
	}

	@Override
	public Map<String, Object> add(User user, User loginUser) {
		Boolean result = false;
		String message = "";
		User userfind = userMapper.findUserByUserName(user.getUserName());
		if (userfind == null) {
			user.setUpdater(loginUser.getUserName());
			user.setCreator(loginUser.getUserName());
			user.setUpdateTime(new Date());
			user.setCreateTime(new Date());
			user.setIsdel("N");
			int i = userMapper.insert(user);
			result = (i == 0) ? false : true;
			message = (result == true) ? "添加用户成功！" : "添加用户失败！";
		} else {
			message = "该用户名已被注册！";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("success", result);
		map.put("message", message);
		return map;
	}

	@Override
	public Map<String, Object> delete(String id) {
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.deleteByPrimaryKey(Integer.valueOf(id));
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

	@Override
	public Map<String, Object> update(User user, User loginUser) {
		user.setUpdater(loginUser.getUserName());
		user.setUpdateTime(new Date());
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.updateByPrimaryKeySelective(user);
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

	@Override
	public User select(String id) {
		User user = userMapper.selectByPrimaryKey(Integer.valueOf(id));
		return user;
	}

	@Override
	public List<User> selectAll() {
		return userMapper.selectAll();
	}

	@Override
	public Boolean isSystemUser(Integer id) {
		User user = userMapper.selectUserRoleType(id);
		if (user != null && user.getId() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> initUserPassword(String id) {
		Boolean result = false;
		String message = "";
		int i = userMapper.initUserPassword(id);
		result = (i == 0) ? false : true;
		message = (result == true) ? "密码初始化成功！" : "密码初始化失败！";
		Map<String, Object> map = new HashMap<>();
		map.put("success", result);
		map.put("message", message);
		return map;
	}

	public Map<String, Object> updatePassword(String id, String password, String newPassword) {
		User userFind = userMapper.selectByPrimaryKey(Integer.valueOf(id));
		Map<String, Object> result = authUser(userFind,
				new User(null, id, password, null, null, null, null, null, null));
		Boolean authUser = (Boolean) result.get("result");
		if (!authUser) {
			// 返回用户名或密码错误，修改密码失败
			result.put("result", false);
			result.put("message", "原用户名或密码错误，请检查！");
			return result;
		}
		userMapper.updatePassword(id, newPassword);
		result.put("result", true);
		result.put("message", "更新密码成功！");
		return result;
	}

	private Map<String, Object> authUser(User userFind, User userNew) {
		String message = null;
		boolean success = true;

		if (userFind == null) { // 系统中没查到用户输入的用户名的相关信息
			message = "用户名不存在或已被禁用！";
			success = false;
		} else if (!userFind.getPassword().equals(userNew.getPassword())) { // 用户名或密码输入不正确
			message = "用户名或密码不正确！";
			success = false;
		} else {
			success = true;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("success", success);
		return map;
	}

	@Override
	public String execRecordExport(String path) {
		
		List<User> recordList = userMapper.selectAll();
		path = (path == null || path.isEmpty()) ? "D:\\PMS" : path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String classPath = path.replace("%20", " ") + "/" + CommonUtil.getNoFormatTimestamp() + "userInfo.xls";
		List<String> header = getHeader();
		List<String> columns = getColumns();
		exportExcel(recordList, columns, header, classPath);
		// FileResult result = new FileResult (classPath);
		// new File("application/vnd.ms-excel", classPath);
		return classPath;
	}

	private void exportExcel(List<User> recordList, List<String> columns, List<String> headerList, String classPath) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle headerStyle = ExcelFormat.getHeaderStyle(wb);
		HSSFCellStyle bodyStyle = ExcelFormat.getBodyStyle(wb);
		HSSFSheet sheet = wb.createSheet();
		// 构建表头
		HSSFRow headeRow = sheet.createRow(0);
		HSSFCell cell = null;
		for (int i = 0; i < headerList.size(); i++) {
			cell = headeRow.createCell(i);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(headerList.get(i));
			sheet.setColumnWidth(i, 20 * 256);
		}
		// 构建表体
		if (recordList != null && recordList.size() > 0) {
			for (int j = 0; j < recordList.size(); j++) {
				HSSFRow bodyRow = sheet.createRow(j + 1);
				// 获取每行数据
				User re = recordList.get(j);
				Map<String, Object> reqRecord = CommonUtil.object2Map(re);
				for (int row = 0; row < columns.size(); row++) {
					// 循环赋值，需要注意参数顺序
					cell = bodyRow.createCell(row);
					cell.setCellStyle(bodyStyle);
					String value = String.valueOf(reqRecord.get(columns.get(row)));
					if (value.isEmpty() || value.equals("null")) {
						value = "";
					}
					cell.setCellValue(value);
				}
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(classPath);
			wb.write(fos);
			fos.close();
		} catch (Exception e) {
			log.error("导出表格出错", e);
		}
	}

	private List<String> getColumns() {
		List<String> columns = new ArrayList<String>();
		columns.add("userName");
		columns.add("comments");
		columns.add("creator");
		columns.add("createTime");
		columns.add("updater");
		columns.add("updateTime");
		return columns;
	}

	private List<String> getHeader() {
		List<String> header = new ArrayList<String>();
		header.add("用户名");
		header.add("备注");
		header.add("创建者");
		header.add("创建时间");
		header.add("更新者");
		header.add("更新时间");
		return header;
	}

}
