package com.hqgd.pms.service.dataAcquisition.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hqgd.pms.hbase.HbaseConfig;
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
	@Autowired
	private HbaseConfig con;

	@Override
	public Map<String, Object> getHistoricalData(QueryParametersVo queryVo) {
		List<DataAcquisitionVo> historicalDataList = new ArrayList<DataAcquisitionVo>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int total = queryVo.getTotal();
		String tableName = "ns1:hq_equipment_monitor_data";
		String startRow;
		String endRow;
		String state = queryVo.getState();
		int limit = queryVo.getLimit();
		int page = queryVo.getPage();
		Table table = null;
		Connection connection = null;
		try {
			startRow = String.valueOf(CommonUtil.dateToStamp(queryVo.getStartTime()));
			endRow = String.valueOf(Long.valueOf(CommonUtil.dateToStamp(queryVo.getEndTime())) + 1);
			connection = ConnectionFactory.createConnection(con.configuration());
			table = connection.getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
					Bytes.toBytes("EQUIPMENT_ID"), CompareOp.EQUAL, Bytes.toBytes(queryVo.getEquipmentId()));
			filterList.addFilter(filter1);
			if (state.equals("1")) {
				SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
						Bytes.toBytes("STATE"), CompareOp.NOT_EQUAL, Bytes.toBytes("5"));
				filterList.addFilter(filter2);
			}
			scan.setFilter(filterList);
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(endRow));
			scan.setMaxVersions();
			long in = System.currentTimeMillis();
			ResultScanner rs0 = table.getScanner(scan);
			long out = System.currentTimeMillis();
			System.out.println("历史数据Hbase查询耗时:" + (out - in) + "ms ");
			total = (total == 0) ? getTotal(rs0, page) : total;
			long in1 = System.currentTimeMillis();
			ResultScanner rs = table.getScanner(scan);
			long out1 = System.currentTimeMillis();
			System.out.println("历史数据Hbase查询耗时:" + (out1 - in1) + "ms ");
			Result[] rArray = rs.next(page * limit);
			if (rArray.length > 0) {
				int start = 0;
				int end = 0;
				if (page == 1) {
					start = 0;
					if (total < page * limit) {
						end = total % (page * limit);
					} else {
						end = page * limit;
					}
				} else {
					start = (page - 1) * limit;
					end = (page - 1) * limit + total % ((page - 1) * limit);
				}
				for (int i = start; i < end; i++) {
					Result r = rArray[i];
					List<Cell> lc = r.listCells();
					DataAcquisitionVo d = new DataAcquisitionVo();
					d.setAddress(Bytes.toString(lc.get(0).getValue()));
					d.setChannelNum(Bytes.toString(lc.get(1).getValue()));
					d.setEquipmentId(Bytes.toString(lc.get(3).getValue()));
					d.setEquipmentName(Bytes.toString(lc.get(4).getValue()));
					d.setMessage(Bytes.toString(lc.get(5).getValue()));
					d.setOpticalFiberPosition(Bytes.toString(lc.get(6).getValue()));
					d.setPd(Bytes.toString(lc.get(7).getValue()));
					d.setReceiveTime(Bytes.toString(lc.get(8).getValue()));
					d.setState(Integer.valueOf(Bytes.toString(lc.get(9).getValue())));
					d.setTemperature(Bytes.toString(lc.get(11).getValue()));
					d.setUv(Bytes.toString(lc.get(12).getValue()));
					historicalDataList.add(d);
				}
			}

			resultMap.put("data", historicalDataList);
			resultMap.put("total", total);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultMap;

	}

	private int getTotal(ResultScanner rs, int page) {
		if (page == 1) {
			int i = 0;
			for (Result r : rs) {
				i++;
			}
			return i;
		}
		return 0;
	}

	@Override
	public Map<String, Object> historicalCurve(QueryParametersVo queryVo) throws ParseException {
		String flag = "h";
		Map<String, Object> resultmap = curveDate(queryVo, flag);
		return resultmap;
	}

	@Override
	public Map<String, Object> realTimeCurve(QueryParametersVo queryVo) throws ParseException {
		String flag = "r";
		Map<String, Object> resultmap = curveDate(queryVo, flag);
		return resultmap;
	}

	private Map<String, Object> curveDate(QueryParametersVo queryVo, String flag) {
		List<DataAcquisitionVo> dataList = new ArrayList<DataAcquisitionVo>();
		Map<String, Object> resultmap = new HashMap<String, Object>();
		String tableName = "ns1:hq_equipment_monitor_data";
		Table table = null;
		Connection connection = null;
		String startRow;
		String endRow;
		String equipmentId = queryVo.getEquipmentId();
		String state = queryVo.getState();
		try {
			startRow = String.valueOf(CommonUtil.dateToStamp(queryVo.getStartTime()));
			endRow = String.valueOf(Long.valueOf(CommonUtil.dateToStamp(queryVo.getEndTime())) + 1);
			connection = ConnectionFactory.createConnection(con.configuration());
			table = connection.getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
					Bytes.toBytes("EQUIPMENT_ID"), CompareOp.EQUAL, Bytes.toBytes(equipmentId));
			filterList.addFilter(filter1);
			if (flag.equals("h") && state.equals("1")) {
				SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
						Bytes.toBytes("STATE"), CompareOp.NOT_EQUAL, Bytes.toBytes("5"));
				filterList.addFilter(filter2);
			}
			scan.setFilter(filterList);
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(endRow));
			long in = System.currentTimeMillis();
			ResultScanner rs = table.getScanner(scan);
			long out = System.currentTimeMillis();
			System.out.println("数据曲线Hbase查询耗时:" + (out - in) + "ms ");
			for (Result r : rs) {
				DataAcquisitionVo d = new DataAcquisitionVo();
				List<Cell> lc = r.listCells();
				d.setAddress(Bytes.toString(lc.get(0).getValue()));
				d.setChannelNum(Bytes.toString(lc.get(1).getValue()));
				d.setEquipmentId(Bytes.toString(lc.get(3).getValue()));
				d.setEquipmentName(Bytes.toString(lc.get(4).getValue()));
				d.setReceiveTime((Bytes.toString(lc.get(8).getValue())));
				d.setState(Integer.valueOf(Bytes.toString(lc.get(9).getValue())));
				d.setTemperature(Bytes.toString(lc.get(11).getValue()));
				dataList.add(d);
			}
			resultmap = transformState(dataList);
			if (dataList.size() == 0) {
				String equipmentName = equipmentInfoMapper.selectByPrimaryKey(equipmentId).getEquipmentName();
				resultmap.put("equipmentName", equipmentName);
			} else {
				resultmap.put("equipmentName", dataList.get(0).getEquipmentName());
				if (flag.equals("r")) {
					List<DataAcquisitionVo> rtdl = new ArrayList<DataAcquisitionVo>(dataList);
					List<DataAcquisitionVo> rtl = new ArrayList<DataAcquisitionVo>();
					// 重写DataAcquisitionVo方法，按时间逆序排列
					Collections.sort(rtdl);
					for (int i = 0; i < rtdl.size(); i++) {
						// 逆序排列后，第一条数据的时间肯定是最新的
						String rt = rtdl.get(0).getReceiveTime();
						// 把最新时间的数据挑出来放在list里，就是最新数据
						if (rtdl.get(i).getReceiveTime().equals(rt)) {
							rtl.add(rtdl.get(i));
						}
					}
					resultmap.put("realTimeDate", rtl);
				}
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultmap;
	}

	private Map<String, Object> transformState(List<DataAcquisitionVo> historicalDataList) {
		Map<String, Object> resultmap = new HashMap<>();
		List<Map<String, List<List<Double>>>> result = new ArrayList<Map<String, List<List<Double>>>>();
		Map<String, List<List<Double>>> map = new HashMap<>();

		List<Double> allTemperatures = new ArrayList<Double>();
		if (historicalDataList.size() > 0) {
			for (int i = 0; i < historicalDataList.size(); i++) {
				DataAcquisitionVo dv = historicalDataList.get(i);
				String ch = dv.getChannelNum();
				Double receTime = 0.0;
				try {
					receTime = (double) CommonUtil.dateToStamp(dv.getReceiveTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				;
				Double tem = Double.valueOf(dv.getTemperature());
				List<Double> l1 = new ArrayList<Double>();
				l1.add(receTime);
				l1.add(tem);
				allTemperatures.add(tem);
				if (map.containsKey(ch)) {
					map.get(ch).add(l1);
				} else {
					List<List<Double>> ll = new ArrayList<List<Double>>();
					ll.add(l1);
					map.put(ch, ll);
				}
			}
			result.add(map);
		} else {
			resultmap.put("data", null);
		}
		resultmap.put("data", result);
		resultmap.put("temperatures", allTemperatures);
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
		for (int i = 0; i < L.size() - 1; i++) {
			if (i == 0 && L.get(i).getState() != 5) {
				StaticFailCounts(yesterday, L.get(i), table);
			}

			String equipmentId = L.get(i).getEquipmentId();
			String channelNum = L.get(i).getChannelNum();
			int state = L.get(i).getState();
			String equipmentIda = L.get(i + 1).getEquipmentId();
			String channelNuma = L.get(i + 1).getChannelNum();
			int statea = L.get(i + 1).getState();
			// 逐条判断前一条数据和下一条数据，如果ID相同，就说明是同一个设备
			// 继续判断通道号，通道号不同，就说明上一个通道统计结束，可以更新数据库了
			// 当状态不等且下一条不是正常，就说明有新的故障产生，需要统计
			if (channelNum.equals(channelNuma) && equipmentId.equals(equipmentIda)) {
				if (state != statea && statea != 5) {
					switch (statea) {
					case 2:
						comm++;
						break;
					case 3:
						fiber++;
						break;
					case 4:
						therm++;
						break;
					case 9:
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
					sf.setDate(CommonUtil.getDateBySimpleFormatTimestamp(L.get(i).getReceiveTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				staticFailuresMapper.insert(sf);
				comm = 0;
				fiber = 0;
				therm = 0;
				overT = 0;
				if (statea != 5) {
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
					sf.setDate(CommonUtil.getDateBySimpleFormatTimestamp(L.get(i + 1).getReceiveTime()));
				} catch (ParseException e) {
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
		if (y1.getState() != d.getState()) {
			switch (d.getState()) {
			case 2:
				comm++;
				break;
			case 3:
				fiber++;
				break;
			case 4:
				therm++;
				break;
			case 9:
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
	public void staticFailuerF(List<DataAcquisitionVo> t, List<DataAcquisitionVo> y) {
		int comm = 0;// 458通信故障
		int fiber = 0;// 光纤故障
		int therm = 0;// 测温仪故障
		int overT = 0;// 超温故障
		if (t != null && y != null) {
			// 先判断每个设备每个通道的今天的第一条数据和昨天的最后一条数据状态是否一致，不一致且今天故障，次数就+1，一致，就不加
			for (int i = 0; i < t.size(); i++) {
				String equipmentId = t.get(i).getEquipmentId();
				String channelNum = t.get(i).getChannelNum();
				int state = t.get(i).getState();
				String equipmentIdy = y.get(i).getEquipmentId();
				String channelNumy = y.get(i).getChannelNum();
				int statey = y.get(i).getState();
				if (equipmentId.equals(equipmentIdy) && channelNum.equals(channelNumy) && state != statey
						&& state != 5) {
					switch (state) {
					case 2:
						comm++;
						break;
					case 3:
						fiber++;
						break;
					case 4:
						therm++;
						break;
					case 9:
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
					try {
						sf.setDate(CommonUtil.getDateBySimpleFormatTimestamp(t.get(i).getReceiveTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
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
				int state = t.get(i).getState();
				if (state != 5) {
					switch (state) {
					case 2:
						comm++;
						break;
					case 3:
						fiber++;
						break;
					case 4:
						therm++;
						break;
					case 9:
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

	@Override
	public void insertHbase(DataAcquisitionVo d) throws IOException {
		int state = d.getState();
		int failFlag = (state == 5) ? 1 : 0;
		Table table = null;
		Connection connection = null;
		String tableName = "ns1:hq_equipment_monitor_data";
		connection = ConnectionFactory.createConnection(con.configuration());
		table = connection.getTable(TableName.valueOf(tableName));
		String rowkey = "";
		try {
			rowkey = CommonUtil.dateToStamp(d.getReceiveTime()) + d.getEquipmentId() + failFlag + d.getState()
					+ d.getChannelNum();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Put put = new Put(Bytes.toBytes(rowkey));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("EQUIPMENT_ID"), Bytes.toBytes(d.getEquipmentId()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("EQUIPMENT_NAME"), Bytes.toBytes(d.getEquipmentName()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("ADDRESS"), Bytes.toBytes(d.getAddress()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("CHANNEL_NUM"), Bytes.toBytes(d.getChannelNum()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("OPTICAL_FIBER_POSITION"),
				Bytes.toBytes(d.getOpticalFiberPosition()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("TEMPERATURE"), Bytes.toBytes(d.getTemperature()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("PD"), Bytes.toBytes(d.getPd()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("UV"), Bytes.toBytes(d.getUv()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("STATE"), Bytes.toBytes(String.valueOf(d.getState())));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("MESSAGE"), Bytes.toBytes(d.getMessage()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("RECEIVE_TIME"), Bytes.toBytes(d.getReceiveTime()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("DUTY_PERSON"), Bytes.toBytes(d.getDutyPerson()));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("TEL"), Bytes.toBytes(d.getTel()));
		table.put(put);
	}

}
