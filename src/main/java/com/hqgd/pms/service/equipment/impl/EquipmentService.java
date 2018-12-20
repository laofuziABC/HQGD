package com.hqgd.pms.service.equipment.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.ExcelFormat;
import com.hqgd.pms.common.HttpProtocolHandler;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.GeoCode;
import com.hqgd.pms.domain.GeoCodeInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IEquipmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EquipmentService implements IEquipmentService {

	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private static final String baseUrl = "https://restapi.amap.com/v3/geocode/geo?";
	private static final String key = "aae78042bf29a967036e55a46b70a34a";

	@Override
	public Map<String, Object> add(EquipmentInfo equipmentInfo, User loginUser, String city) {
		Map<String, Object> resultMap = new HashMap<>();
		Boolean result = false;
		EquipmentInfo equipFind = equipmentInfoMapper.selectByPrimaryKey(equipmentInfo.getEquipmentId());
		if (equipFind == null) {
			equipmentInfo.setUpdater(loginUser.getUserName());
			equipmentInfo.setCreator(loginUser.getUserName());
			equipmentInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
			equipmentInfo.setCreateTime(CommonUtil.getSimpleFormatTimestamp());
			String address = equipmentInfo.getAddress();
			GeoCode geoCode = geocode(address, city);
			if (geoCode == null) {
				resultMap.put("message", "未找到该地址，请输入正确的地址");
			} else {
				String latlon = geoCode.getLocation();
				equipmentInfo.setLngLat(latlon);
				int i = equipmentInfoMapper.insert(equipmentInfo);
				result = (i == 0) ? false : true;
				resultMap.put("message", "添加设备成功");
			}

		} else {
			resultMap.put("message", "该设备ID已经存在");
		}
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000001");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		return resultMap;
	}

	private GeoCode geocode(String address, String city) {
		city = city.substring(0, city.length() - 1);
		String url = baseUrl + "key=" + key + "&address=" + address + "&city=" + city;
		String resultJson = HttpProtocolHandler.httpsRequest(url, "GET", null);
		Gson gson = new Gson();
		GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
		geoCodeInfo = gson.fromJson(resultJson, geoCodeInfo.getClass());
		if (geoCodeInfo.getGeocodes().size() < 1) {
			return null;
		}
		GeoCode geoCode = geoCodeInfo.getGeocodes().get(0);
		return geoCode;
	}

	@Override
	public Map<String, Object> delete(String equipmentId) {
		Map<String, Object> resultMap = new HashMap<>();
		int i = equipmentInfoMapper.deleteByPrimaryKey(equipmentId);
		Boolean result = (i == 0) ? false : true;
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000002");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		return resultMap;
	}

	@Override
	public Map<String, Object> update(EquipmentInfo equipmentInfo, User loginUser, String city) {
		Map<String, Object> resultMap = new HashMap<>();
		Boolean result = false;
		equipmentInfo.setUpdater(loginUser.getUserName());
		equipmentInfo.setCreator(loginUser.getUserName());
		equipmentInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
		equipmentInfo.setCreateTime(CommonUtil.getSimpleFormatTimestamp());
		String address = equipmentInfo.getAddress();
		GeoCode geoCode = geocode(address, city);
		if (geoCode == null) {
			resultMap.put("message", "未找到该地址，请输入正确的地址");
		} else {
			String latlon = geoCode.getLocation();
			equipmentInfo.setLngLat(latlon);
			int i = equipmentInfoMapper.updateByPrimaryKeySelective(equipmentInfo);
			result = (i == 0) ? false : true;
			resultMap.put("message", "添加设备失败");
		}
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000003");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		return resultMap;
	}

	@Override
	public EquipmentInfo select(String equipmentId) {
		EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(equipmentId);
		return equipmentInfo;
	}

	@Override
	public List<EquipmentInfo> selectAllByUser(String userName) {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAllEquipmentByUser(userName);
		return equipmentInfoList;
	}

	@Override
	public List<EquipmentInfo> selectAll() {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAll();
		return equipmentInfoList;
	}

	@Override
	public List<EquipmentInfo> selectAllByAddress(String adcode) {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAllEquipmentByAddress(adcode);
		return equipmentInfoList;
	}

	@Override
	public EquipmentInfo selectByEquipmentName(String equipmentName) {
		EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByEquipmentName(equipmentName);
		return equipmentInfo;
	}

	@Override
	public String execRecordExport(String path) {

		List<EquipmentInfo> recordList = equipmentInfoMapper.selectAll();
		path = (path == null || path.isEmpty()) ? "D:\\PMS" : path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String classPath = path.replace("%20", " ") + "/" + CommonUtil.getNoFormatTimestamp() + "equipmentInfo.xls";
		List<String> header = getHeader();
		List<String> columns = getColumns();
		exportExcel(recordList, columns, header, classPath);
		// FileResult result = new FileResult (classPath);
		// new File("application/vnd.ms-excel", classPath);
		return classPath;
	}

	private void exportExcel(List<EquipmentInfo> recordList, List<String> columns, List<String> headerList,
			String classPath) {
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
				EquipmentInfo re = recordList.get(j);
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
		columns.add("equipmentId");
		columns.add("equipmentName");
		columns.add("userName");
		columns.add("channelTem");
		columns.add("address");
		columns.add("adcode");
		columns.add("lngLat");
		columns.add("frameStru");
		columns.add("creator");
		columns.add("createTime");
		columns.add("updater");
		columns.add("updateTime");
		return columns;
	}

	private List<String> getHeader() {
		List<String> columns = new ArrayList<String>();
		columns.add("设备ID");
		columns.add("设备名称");
		columns.add("所属用户");
		columns.add("通道号-温度");
		columns.add("安装地址");
		columns.add("区域编码");
		columns.add("经纬度");
		columns.add("帧结构");
		columns.add("创建者");
		columns.add("创建时间");
		columns.add("更新者");
		columns.add("更新时间");
		return columns;
	}

	// @Override
	// public List<EquipmentParam> selectEquipmentParam(String equipmentId) {
	// List<EquipmentParam> equipmentParamList =
	// equipmentParamMapper.selectEquipmentParam(equipmentId);
	// return equipmentParamList;
	// }

	// @Override
	// public Boolean setEquipmentParam(EquipmentParam equipmentParam, User
	// loginUser) {
	// equipmentParam.setCreator(loginUser.getUserName());
	// equipmentParam.setCreateTime(new Date());
	// equipmentParam.setUpdater(loginUser.getUserName());
	// equipmentParam.setUpdateTime(new Date());
	// int i = equipmentParamMapper.setEquipmentParam(equipmentParam);
	// Boolean result = (i == 0) ? false : true;
	// // Map<String, Object> resultMap = new HashMap<String, Object>();
	// // resultMap.put("success", result);
	// // resultMap.put("resultCode", "00000007");
	// // resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
	// // resultMap.put("message", "");
	// return result;
	// }

	// @Override
	// public Map<String, Object> updateParam(EquipmentParam equipmentParam, User
	// loginUser) {
	// equipmentParam.setUpdater(loginUser.getUserName());
	// equipmentParam.setUpdateTime(new Date());
	// int i = equipmentParamMapper.updateParam(equipmentParam);
	// Boolean result = (i == 0) ? false : true;
	// Map<String, Object> resultMap = new HashMap<String, Object>();
	// resultMap.put("success", result);
	// resultMap.put("resultCode", "00000008");
	// resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
	// resultMap.put("message", "");
	// return resultMap;
	// }

}
