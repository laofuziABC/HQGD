package com.hqgd.pms.service.dataAcquisition.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.dataAcquisition.StaticFailuresMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.domain.StaticFailures;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataAcquisitionService implements IDataAcquisitionService {
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private StaticFailuresMapper staticFailuresMapper;

	@Override
	public List<DataAcquisitionVo> execGetRealTimeData(String equipmentId) {
		List<DataAcquisitionVo> realTimeDateList = null;
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", equipmentId);
		String type = equipmentInfoMapper.selectTypeById(equipmentId);
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_r1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_r2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_r3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_r4");
			break;
		}
		realTimeDateList = dataAcquisitionVoMapper.selectRealTimeDataById(param);
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
						// String max = ta[1].substring(1, ta[1].length() - 1);
						// String min = ta[2].substring(1, ta[2].length() - 1);
						String max = ta[2].substring(1, ta[2].length() - 1);
						String min = ta[3].substring(1, ta[3].length() - 1);
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
		String type = equipmentInfoMapper.selectTypeById(queryVo.getEquipmentId());
		List<DataAcquisitionVo> historicalDataList = null;
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_4");
			break;
		}
		historicalDataList = dataAcquisitionVoMapper.selectHistoricalDataById(param);
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
		String type = equipmentInfoMapper.selectTypeById(queryVo.getEquipmentId());
		Integer total = null;
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_4");
			break;
		}
		total = dataAcquisitionVoMapper.selectTotal(param);
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
		EquipmentInfo equipment = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId());
		String type = equipment.getType();
		// String type = equipmentInfoMapper.selectTypeById(queryVo.getEquipmentId());
		List<DataAcquisitionVo> historicalDataList = null;
		long inTime = System.currentTimeMillis();
		log.info("查询数据SQL开始：" + inTime);
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_4");
			break;
		}
		historicalDataList = dataAcquisitionVoMapper.selectHistoricalCurveById(param);
		long outTime = System.currentTimeMillis();
		log.info("SQL结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("时长为：" + midTime);

		List<String> channelNumArr = new ArrayList<>();// 通道号数组
		List<List<Float>> channelTemArr = new ArrayList<List<Float>>();// 通道号温度数数组
		List<String> tem = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (historicalDataList.isEmpty()) {
			return map;
		}
		DataAcquisitionVo vo = historicalDataList.get(0);
		// String equipmentId = queryVo.getEquipmentId();
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
		}
		long outTime1 = System.currentTimeMillis();
		log.info("处理数据结束：" + outTime1);
		long midTime1 = outTime1 - inTime1;
		log.info("时长为：" + midTime1);
		map.put("equipment", equipment);
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

		String startTime = queryVo.getStartTime();
		String endTime = queryVo.getEndTime();
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		EquipmentInfo equipment = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId());
		String type = equipment.getType();
		List<DataAcquisitionVo> historicalDataList = null;
		long inTime = System.currentTimeMillis();
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_r1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_r2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_r3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_r4");
			break;
		}
		historicalDataList = dataAcquisitionVoMapper.selectHistoricalCurveById(param);
		long outTime = System.currentTimeMillis();
		log.info("查询periodDate时长为：" + (outTime - inTime));

		List<String> channelNumArr = new ArrayList<>();// 通道号数组
		List<List<Float>> channelTemArr = new ArrayList<List<Float>>();// 通道号温度数数组
		List<String> tem = new ArrayList<>();
		List<String> receiveTime = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if (historicalDataList.isEmpty()) {
			channelNumArr = dataAcquisitionVoMapper.selectAllChannels(param);
		} else {
			DataAcquisitionVo vo = historicalDataList.get(0);
			receiveTime = Arrays.asList(vo.getReceiveTime().split(","));
			for (int i = 0; i < historicalDataList.size(); i++) {
				channelNumArr.add(historicalDataList.get(i).getChannelNum());
				tem = Arrays.asList(historicalDataList.get(i).getTemperature().split(","));
				// 避免空指针的情况下，将List<String>更改为List<Float>
				if (tem.size() > 0) {
					List<Float> temFloat = tem.stream().map(Float::parseFloat).collect(Collectors.toList());
					channelTemArr.add(temFloat);
				}
			}
		}
		map.put("equipment", equipment);
		map.put("timeList", receiveTime);
		map.put("channelList", channelNumArr);
		map.put("dataList", channelTemArr);
		return map;
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

	@Override
	public List<DataAcquisitionVo> record(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		param.put("state", queryVo.getState());
		String type = equipmentInfoMapper.selectTypeById(queryVo.getEquipmentId());
		List<DataAcquisitionVo> historicalDataList = null;
		switch (type) {
		case "1":
			param.put("table", "hq_equipment_monitor_data_1");
			break;
		case "2":
			param.put("table", "hq_equipment_monitor_data_2");
			break;
		case "3":
			param.put("table", "hq_equipment_monitor_data_3");
			break;
		case "4":
			param.put("table", "hq_equipment_monitor_data_4");
			break;
		}
		historicalDataList = dataAcquisitionVoMapper.recordExport(param);
		return historicalDataList;
	}

	@Override
	public List<StaticFailures> errorStateStatic(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", queryVo.getStartTime());
		String endTime = queryVo.getEndTime();
		try {
			endTime = getAfterDay(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		param.put("endTime", endTime);
		List<StaticFailures> errorStateStatic = staticFailuresMapper.errorStateStatic(param);
		return errorStateStatic;
	}

	int comm = 0;// 458通信故障
	int fiber = 0;// 光纤故障
	int therm = 0;// 测温仪故障
	int overT = 0;// 超温故障

	@Override
	public void run(String startTime) {
		log.info("startTime =" + startTime);
		Map<String, Object> param = new HashMap<>();
		param.put("startTime", startTime + " 00:00:00");
		param.put("endTime", startTime + " 23:59:59");
		// 获取前一天的时间
		String yesterday = null;
		try {
			yesterday = CommonUtil.getBeforeDay(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// L表 ——表示查询出一天的数据，然后批量处理
		param.put("table", "hq_equipment_monitor_data_1");
		List<DataAcquisitionVo> L1 = dataAcquisitionVoMapper.selectAllFailures(param);
		staticFailuer(L1, yesterday, "hq_equipment_monitor_data_1");

		param.put("table", "hq_equipment_monitor_data_2");
		List<DataAcquisitionVo> L2 = dataAcquisitionVoMapper.selectAllFailures(param);
		staticFailuer(L2, yesterday, "hq_equipment_monitor_data_2");

		param.put("table", "hq_equipment_monitor_data_3");
		List<DataAcquisitionVo> L3 = dataAcquisitionVoMapper.selectAllFailures(param);
		staticFailuer(L3, yesterday, "hq_equipment_monitor_data_3");

		param.put("table", "hq_equipment_monitor_data_4");
		List<DataAcquisitionVo> L4 = dataAcquisitionVoMapper.selectAllFailures(param);
		staticFailuer(L4, yesterday, "hq_equipment_monitor_data_4");

	}

	// 统计一天的故障，除去每个通道的第一次记录
	private void staticFailuer(List<DataAcquisitionVo> L, String yesterday, String table) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < L.size() - 1; i++) {
			if (i == 0 && !L.get(i).getState().equals("5")) {
				StaticFailCounts(yesterday, L.get(i), table);
			}

			String equipmentId = L.get(i).getEquipmentId();
			String channelNum = L.get(i).getChannelNum();
			String state = L.get(i).getState();
			String equipmentIda = L.get(i + 1).getEquipmentId();
			String channelNuma = L.get(i + 1).getChannelNum();
			String statea = L.get(i + 1).getState();
			// 逐条判断前一条数据和下一条数据，如果ID相同，就说明是同一个设备
			// 继续判断通道号，通道号不同，就说明上一个通道统计结束，可以更新数据库了
			// 当状态不等且下一条不是正常，就说明有新的故障产生，需要统计
			if (channelNum.equals(channelNuma) && equipmentId.equals(equipmentIda)) {
				if (!state.equals(statea) && !statea.equals("5")) {
					switch (statea) {
					case "2":
						comm++;
						break;
					case "3":
						fiber++;
						break;
					case "4":
						therm++;
						break;
					case "9":
						overT++;
						break;
					}
				}

			} else if (!equipmentId.equals(equipmentIda) || !channelNum.equals(channelNuma)) {
				StaticFailures sf = new StaticFailures();
				sf.setEquipmentId(equipmentId);
				sf.setEquipmentName(L.get(i).getEquipmentName());
				sf.setAddress(L.get(i).getAddress());
				sf.setChannelNum(channelNum);
				sf.setOpticalFiberPosition(L.get(i).getOpticalFiberPosition());
				sf.setCommunicate(comm);
				sf.setFiber(fiber);
				sf.setThermometer(therm);
				sf.setOverTemperature(overT);
				try {
					sf.setDate(format.parse(L.get(i).getReceiveTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				staticFailuresMapper.insert(sf);
				comm = 0;
				fiber = 0;
				therm = 0;
				overT = 0;
				if (!statea.equals("5")) {
					// 如果某个设备的某个通道的第一条数据不是正常状态，就需要判断昨天最后一条是否正常，若不正常则算做一次，若正常，今天的故障就要加1
					StaticFailCounts(yesterday, L.get(i + 1), table);
				}
			}
			if (i == L.size() - 2) {
				StaticFailures sf = new StaticFailures();
				sf.setEquipmentId(equipmentIda);
				sf.setEquipmentName(L.get(i + 1).getEquipmentName());
				sf.setAddress(L.get(i + 1).getAddress());
				sf.setChannelNum(channelNuma);
				sf.setOpticalFiberPosition(L.get(i + 1).getOpticalFiberPosition());
				sf.setCommunicate(comm);
				sf.setFiber(fiber);
				sf.setThermometer(therm);
				sf.setOverTemperature(overT);
				try {
					sf.setDate(format.parse(L.get(i + 1).getReceiveTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				staticFailuresMapper.insert(sf);
				comm = 0;
				fiber = 0;
				therm = 0;
				overT = 0;
			}
		}

	}

	public void StaticFailCounts(String yesterday, DataAcquisitionVo d, String table) {
		Map<String, String> param = new HashMap<>();
		param.put("startTime", yesterday + " 00:00:00");
		param.put("endTime", yesterday + " 23:59:59");
		param.put("equipmentId", d.getEquipmentId());
		param.put("channelNum", d.getChannelNum());
		param.put("table", table);
		// 获取前一天最后一条数据
		DataAcquisitionVo y1 = dataAcquisitionVoMapper.selectYesDayFailures(param);
		if (!y1.getState().equals(d.getState())) {
			switch (d.getState()) {
			case "2":
				comm++;
				break;
			case "3":
				fiber++;
				break;
			case "4":
				therm++;
				break;
			case "9":
				overT++;
				break;
			}
		}
	}

	public String getAfterDay(String today) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat("yy-MM-dd").parse(today));
		int date = c.get(Calendar.DATE);
		c.set(Calendar.DATE, date + 1);
		String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		System.out.println(afterDay);
		return afterDay;
	}

	/**
	 * 描述： 统计每天第一组数据，并将结果保存数据库 作者：姚绒 日期：2019年3月14日 下午4:41:43
	 *
	 */
	public void staticFailuerF(List<DataAcquisitionVo> t, List<DataAcquisitionVo> y) throws ParseException {
		int comm = 0;// 458通信故障
		int fiber = 0;// 光纤故障
		int therm = 0;// 测温仪故障
		int overT = 0;// 超温故障
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (t != null && y != null) {
			// 先判断每个设备每个通道的今天的第一条数据和昨天的最后一条数据状态是否一致，不一致且今天故障，次数就+1，一致，就不加
			for (int i = 0; i < t.size(); i++) {
				String equipmentId = t.get(i).getEquipmentId();
				String channelNum = t.get(i).getChannelNum();
				String state = t.get(i).getState();
				String equipmentIdy = y.get(i).getEquipmentId();
				String channelNumy = y.get(i).getChannelNum();
				String statey = y.get(i).getState();
				if (equipmentId.equals(equipmentIdy) && channelNum.equals(channelNumy) && !state.equals(statey)
						&& !state.equals("5")) {
					switch (state) {
					case "2":
						comm++;
						break;
					case "3":
						fiber++;
						break;
					case "4":
						therm++;
						break;
					case "9":
						overT++;
						break;
					}
				} else {

					StaticFailures sf = new StaticFailures();
					sf.setEquipmentId(equipmentId);
					sf.setEquipmentName(t.get(i).getEquipmentName());
					sf.setAddress(t.get(i).getAddress());
					sf.setChannelNum(channelNum);
					sf.setOpticalFiberPosition(t.get(i).getOpticalFiberPosition());
					sf.setCommunicate(comm);
					sf.setFiber(fiber);
					sf.setThermometer(therm);
					sf.setOverTemperature(overT);
					sf.setDate(format.parse(t.get(i).getReceiveTime()));
					staticFailuresMapper.insert(sf);
					comm = 0;
					fiber = 0;
					therm = 0;
					overT = 0;
				}

			}
		} else if (t != null && y == null) {
			for (int i = 0; i < t.size(); i++) {
				String equipmentId = t.get(i).getEquipmentId();
				String channelNum = t.get(i).getChannelNum();
				String state = t.get(i).getState();
				if (state != "5") {
					switch (state) {
					case "2":
						comm++;
						break;
					case "3":
						fiber++;
						break;
					case "4":
						therm++;
						break;
					case "9":
						overT++;
						break;
					}
				}

				StaticFailures sf = new StaticFailures();
				sf.setEquipmentId(equipmentId);
				sf.setEquipmentName(t.get(i).getEquipmentName());
				sf.setAddress(t.get(i).getAddress());
				sf.setChannelNum(channelNum);
				sf.setOpticalFiberPosition(t.get(i).getOpticalFiberPosition());
				sf.setCommunicate(comm);
				sf.setFiber(fiber);
				sf.setThermometer(therm);
				sf.setOverTemperature(overT);
				staticFailuresMapper.insert(sf);
				comm = 0;
				fiber = 0;
				therm = 0;
				overT = 0;
			}
		}
	}

}
