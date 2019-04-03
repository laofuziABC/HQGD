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
import com.hqgd.pms.domain.ChannelExtremum;
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
		param.put("state", queryVo.getState());
		EquipmentInfo equipment = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId());
		String type = equipment.getType();

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
		Map<String, Object> resultmap = selectCurveById(param);
		return resultmap;
	}

	private Map<String, Object> selectCurveById(Map<String, Object> param) {
		long inTime = System.currentTimeMillis();
		log.info("查询数据SQL开始：" + inTime);
		List<DataAcquisitionVo> historicalDataList = null;
		historicalDataList = dataAcquisitionVoMapper.selectCurveById(param);
		long outTime = System.currentTimeMillis();
		log.info("查询曲线SQL时长为：" + (outTime - inTime));
		Map<String, Object> resultmap = new HashMap<>();
		List<Map<String, List<List<Double>>>> result = new ArrayList<Map<String, List<List<Double>>>>();
		Map<String, List<List<Double>>> map = new HashMap<>();
		List<List<Double>> ll = new ArrayList<List<Double>>();
		if (historicalDataList.size() > 0) {
			for (int i = 0; i < historicalDataList.size() - 1; i++) {
				DataAcquisitionVo dv = historicalDataList.get(i);
				String ch = dv.getChannelNum();
				String chn = historicalDataList.get(i + 1).getChannelNum();
				Double receTime = CommonUtil.str2Time(dv.getReceiveTime());
				Double tem = Double.valueOf(dv.getTemperature());
				List<Double> l1 = new ArrayList<Double>();
				l1.add(receTime);
				l1.add(tem);
				ll.add(l1);

				if (!ch.equals(chn)) {
					List<List<Double>> lll = new ArrayList<List<Double>>(ll);
					map.put(ch, lll);
					Map<String, List<List<Double>>> map1 = new HashMap<>(map);
					result.add(map1);
					map.clear();
					ll.clear();
				}
				if (i == historicalDataList.size() - 2) {
					DataAcquisitionVo dvl = historicalDataList.get(i + 1);
					String chl = dvl.getChannelNum();
					Double receTimel = CommonUtil.str2Time(dvl.getReceiveTime());
					Double teml = Double.valueOf(dvl.getTemperature());
					List<Double> l1l = new ArrayList<Double>();
					l1l.add(receTimel);
					l1l.add(teml);
					ll.add(l1l);
					map.put(chl, ll);
					result.add(map);
				}
			}
		} else {
			resultmap.put("date", null);
		}
		resultmap.put("date", result);
		return resultmap;
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
		Map<String, Object> resultmap = selectCurveById(param);
		resultmap.put("equipment", equipment);
		return resultmap;
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
		// 检查数据库这一天的数据是否已经统计
		int count = staticFailuresMapper.selectByDate(startTime);
		if (count == 0) {

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
		} else {
			log.info("startTime =" + startTime + "已经统计过了！");
		}
	}

	// 统计一天的故障
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

	/**
	 * 通过设备主键，获取指定时间段内的通道温度最值
	 */
	@Override
	public Map<String, Object> getHistoryExtremums(QueryParametersVo queryVo) {
		String startTime = queryVo.getStartTime();
		String endTime = queryVo.getEndTime();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		long inTime = System.currentTimeMillis();
		EquipmentInfo equipment = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId());
		String type = equipment.getType();
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
		List<ChannelExtremum> extremumList = dataAcquisitionVoMapper.findChannelExtremums(param);
		long outTime = System.currentTimeMillis();
		log.info("查询extremum时长为：" + (outTime - inTime));
		result.put("equipment", equipment);
		result.put("extremumList", extremumList);
		return result;
	}

}
