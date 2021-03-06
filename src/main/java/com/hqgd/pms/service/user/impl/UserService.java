package com.hqgd.pms.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.user.UserMapper;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

/**
 * 描述： 作者：姚绒 日期：2018年11月5日 上午9:43:08
 *
 */
@Service
public class UserService implements IUserService {

	@Resource
	private UserMapper userMapper;

	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public User selectByUserName(String userName) {
		return userMapper.selectByUserName(userName);
	}

	@Override
	public Map<String, Object> add(User user, User loginUser) {
		Boolean result = false;
		String message = "";
		User userfind = userMapper.selectByUserName(user.getUserName());
		if (userfind == null) {
			user.setUpdater(loginUser.getUserName());
			user.setCreator(loginUser.getUserName());
			user.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
			user.setCreateTime(CommonUtil.getSimpleFormatTimestamp());
			int i = userMapper.insert(user);
			result = (i == 0) ? false : true;
			message = (result == true) ? "添加用户成功！" : "添加用户失败！";
		} else if (userfind.getIsdel().equals("Y")) {
			user.setId(userfind.getId());
			int i = userMapper.updateByPrimaryKeySelective(user);
			result = (i == 0) ? false : true;
			message = (result == true) ? "添加用户成功！" : "添加用户失败！";
		} else if (userfind.getIsdel().equals("N")) {
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
		user.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
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
				new User(null, id, null, password, null, null, null, null, null, null));
		Boolean authUser = (Boolean) result.get("success");
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
	public String resetPassword(String userName, String phone, String password, String newPassword) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("tel", phone);
		map.put("password", password);
		map.put("newPassword", newPassword);
		User user = userMapper.selectByPhUn(map);
		if (user == null) {
			return "用户名和联系方式不匹配";
		}
		userMapper.resetPassword(map);
		return "重置成功";
	}

}
