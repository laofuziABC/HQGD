package com.hqgd.pms.service.dataAcquisition.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataAcquisitionService implements IDataAcquisitionService {
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;

	@Override
	public List<DataAcquisitionVo> execGetRealTimeData(String equipmentId) {
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionVoMapper.selectRealTimeDataById(equipmentId);
		if (realTimeDateList.size() > 0) {
			int numOfCh = realTimeDateList.get(0).getNumOfCh();
			if (realTimeDateList.size() == numOfCh) {
				EquipmentInfo e = equipmentInfoMapper.selectByPrimaryKey(equipmentId);
				String s = e.getChannelTem();
				s = s.substring(2, s.length() - 2);
				String[] arr = s.split("\\],\\[");
				if (arr.length == realTimeDateList.size()) {
					for (int i = 0; i < realTimeDateList.size(); i++) {
						String[] ta = arr[i].split(",");
						String cn = ta[0].substring(1, ta[0].length() - 1);
						String max = ta[1].substring(1, ta[1].length() - 1);
						String min = ta[2].substring(1, ta[2].length() - 1);
						String t = realTimeDateList.get(i).getTemperature();
						String channelNum = realTimeDateList.get(i).getChannelNum();
						if (!t.equals("3000") && !t.equals("-437") && !t.equals("2999") && channelNum.equals(cn)
								&& (Float.valueOf(t) < Float.valueOf(min) || Float.valueOf(t) > Float.valueOf(max))) {
							realTimeDateList.get(i).setState("9");
						}
					}
				}

			}
			return realTimeDateList;
		} else {
			return null;
		}

	}

	@Override
	public List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo) {
		int page = queryVo.getPage();
		int limit = queryVo.getLimit();
		int total = limit * page;
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		param.put("limit", queryVo.getLimit());
		param.put("total", total);
		param.put("state", queryVo.getState());
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionVoMapper.selectHistoricalDataById(param);
		for (DataAcquisitionVo d : historicalDataList) {
			switch (d.getTemperature()) {
			case "-437":
				d.setTemperature("故障1");
				break;
			case "3000":
				d.setTemperature("故障2");
				break;
			case "2999.9":
				d.setTemperature("故障3");
				break;
			}
		}
		return historicalDataList;
	}

	@Override
	public Integer selectTotal(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		param.put("state", queryVo.getState());
		Integer total = dataAcquisitionVoMapper.selectTotal(param);
		return total;
	}

	@Override
	public Map<String, Object> historicalCurve(QueryParametersVo queryVo) throws Exception {
		String startTime = queryVo.getStartTime();
		String endTime = queryVo.getEndTime();
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		long inTime = System.currentTimeMillis();
		log.info("查询数据SQL开始：" + inTime);
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionVoMapper.selectHistoricalCurveById(param);
		long outTime = System.currentTimeMillis();
		log.info("SQL结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("时长为：" + midTime);

		List<String> channelNumArr = new ArrayList<>();// 通道号数组
		List<List<Float>> channelTemArr = new ArrayList<List<Float>>();// 通道号温度数数组
		List<String> tem = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		// List<String> state = new ArrayList<>();
		// List<List<String>> stateArr = new ArrayList<>();
		if (historicalDataList.isEmpty()) {
			return map;
		}
		DataAcquisitionVo vo = historicalDataList.get(0);
		String equipmentId = queryVo.getEquipmentId();
		long inTime1 = System.currentTimeMillis();
		log.info("处理数据开始：" + inTime);
		List<String> receiveTime = Arrays.asList(vo.getReceiveTime().split(","));
		for (int i = 0; i < historicalDataList.size(); i++) {
			channelNumArr.add(historicalDataList.get(i).getChannelNum());
			tem = Arrays.asList(historicalDataList.get(i).getTemperature().split(","));
			// 避免空指针的情况下，将List<String>更改为List<Float>
			if (tem.size() > 0) {
				List<Float> temFloat = tem.stream().map(Float::parseFloat).collect(Collectors.toList());
				channelTemArr.add(temFloat);
			}
			// state = Arrays.asList(historicalDataList.get(i).getState().split(","));
			// stateArr.add(state);
		}
		long outTime1 = System.currentTimeMillis();
		log.info("处理数据结束：" + outTime1);
		long midTime1 = outTime1 - inTime1;
		log.info("时长为：" + midTime1);
		map.put("equipmentId", equipmentId);
		map.put("receiveTime", receiveTime);
		map.put("channelNumArr", channelNumArr);
		map.put("channelTemArr", channelTemArr);
		// map.put("stateArr", stateArr);
		return map;
	}

	

	/**
	 * 获取指定时间段（自开机到当前时间）内的数据点的集合 channelList: 所有通道名称集合 各个通道在具体时间段内的采集温度集合
	 * 各个通道在集体时间段内的采集时间集合
	 */
	@Override
	public Map<String, Object> getPeriodDataByQuery(QueryParametersVo queryVo) {
		String equipmentId = queryVo.getEquipmentId();
		if (equipmentId == null || equipmentId.equals("")) {
			return null;
		} else {
			Map<String, Object> param = new HashMap<>();
			param.put("equipmentId", queryVo.getEquipmentId());
			param.put("startTime", queryVo.getStartTime());
			param.put("endTime", queryVo.getEndTime());
			// 使用Map集合封装所有结果
			Map<String, Object> result = new HashMap<String, Object>();
			long inTime = System.currentTimeMillis();
			// 获取所有通道、时间集合
			List<String> channelList = dataAcquisitionVoMapper.selectAllChannels(equipmentId);
			List<Date> timeList = dataAcquisitionVoMapper.selectAllTimestamp(param);
			// 循环封装各个通道的数据集合
			List<List<DataAcquisitionVo>> dataList = new ArrayList<List<DataAcquisitionVo>>();
			for (int i = 0; i < channelList.size(); i++) {
				String channelNum = channelList.get(i).trim();
				param.put("channelNum", channelNum);
				List<DataAcquisitionVo> chanDataList = dataAcquisitionVoMapper.selectChanDataByParam(param);
				dataList.add(chanDataList);
			}
			long outTime = System.currentTimeMillis();
			log.info("查询数据SQL时长为：" + (outTime - inTime));
			result.put("channelList", channelList);
			result.put("timeList", timeList);
			result.put("dataList", dataList);
			return result;
		}
	}


	@Override
	public List<DataAcquisitionVo> allEquipRealtime(String userName, String roleId) {
		List<DataAcquisitionVo> realTimeDateList = null;
		if (roleId.equals("1")) {
			userName = "";
			realTimeDateList = dataAcquisitionVoMapper.selectAllEquipRealtime(userName);
		} else {
			realTimeDateList = dataAcquisitionVoMapper.selectAllEquipRealtime(userName);
		}
		return realTimeDateList;
	}

}
