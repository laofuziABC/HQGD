package com.hqgd.pms.service.dataAcquisition.impl;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.ExcelFormat;
import com.hqgd.pms.dao.dataAcquisition.IDataAcquisitionDao;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataAcquisitionService implements IDataAcquisitionService {
	@Autowired
	@Qualifier("dataAcquisitionDao")
	private IDataAcquisitionDao dataAcquisitionDao;

	@Override
	public List<DataAcquisitionVo> execGetRealTimeData(String equipmentId) {
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionDao.getRealTimeDate(equipmentId);
		return realTimeDateList;
	}

	@Override
	public List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo) {
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionDao.getHistoricalData(queryVo);
		return historicalDataList;
	}

	@Override
	public Map<String, Object> historicalCurve(QueryParametersVo queryVo) {
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionDao.historicalCurve(queryVo);
		List<String> channelNumArr = new ArrayList<>();// 通道号数组
		List<List<String>> channelTemArr = new ArrayList<>();// 通道号温度数数组
		List<String> tem = new ArrayList<>();
//		List<String> state = new ArrayList<>();
//		List<List<String>> stateArr = new ArrayList<>();
		DataAcquisitionVo vo = historicalDataList.get(0);
		String equipmentId = queryVo.getEquipmentId();
		String receiveTime = vo.getReceiveTime();

		if (historicalDataList.size() > 0) {
			for (int i = 0; i < historicalDataList.size(); i++) {

				channelNumArr.add(historicalDataList.get(i).getChannelNum());
				tem = Arrays.asList(historicalDataList.get(i).getTemperature().split(","));
				channelTemArr.add(tem);
//				state = Arrays.asList(historicalDataList.get(i).getState().split(","));
//				stateArr.add(state);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("equipmentId", equipmentId);
		map.put("receiveTime", receiveTime);
		map.put("channelNumArr", channelNumArr);
		map.put("channelTemArr", channelTemArr);
//		map.put("stateArr", stateArr);
		return map;
	}

	@Override
	public String execRecordExport(@Valid QueryParametersVo data) {

		List<DataAcquisitionVo> recordList = dataAcquisitionDao.getHistoricalData(data);
		CommonUtil comm = new CommonUtil();
		String classPath = comm.getDocumentSavePath().replace("%20", " ") + "/" + CommonUtil.getNoFormatTimestamp()
				+ "historicalData.xls";
		List<String> header = getHeader();
		List<String> columns = getColumns();
		exportExcel(recordList, columns, header, classPath);
		// FileResult result = new FileResult (classPath);
		// new File("application/vnd.ms-excel", classPath);
		return classPath;
	}

	private void exportExcel(List<DataAcquisitionVo> recordList, List<String> columns, List<String> headerList,
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
				DataAcquisitionVo re = recordList.get(j);
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
		columns.add("address");
		columns.add("channelNum");
		columns.add("opticalFiberPosition");
		columns.add("temperature");
		columns.add("pd");
		columns.add("uv");
		columns.add("state");
		columns.add("message");
		columns.add("receiveTime");
		columns.add("dutyPerson");
		columns.add("tel");
		return columns;
	}

	private List<String> getHeader() {
		List<String> header = new ArrayList<String>();
		header.add("设备ID");
		header.add("设备名称");
		header.add("地址");
		header.add("通道号");
		header.add("光纤位置");
		header.add("温度");
		header.add("PD值");
		header.add("UV值");
		header.add("工作状态");
		header.add("状态信息");
		header.add("接收时间");
		header.add("值班人员");
		header.add("联系方式");
		return header;
	}

}
