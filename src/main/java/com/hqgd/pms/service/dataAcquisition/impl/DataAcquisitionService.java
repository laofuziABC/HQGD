package com.hqgd.pms.service.dataAcquisition.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import com.hqgd.pms.dao.equipment.RouterInfoMapper;
import com.hqgd.pms.domain.ChannelExtremum;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.domain.StaticFailures;
import com.hqgd.pms.hbase.HbaseConfig;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;
import com.hqgd.pms.service.equipment.IEquipmentService;

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
	@Autowired
	IEquipmentService equipmentService;
	@Resource
	private RouterInfoMapper routerInfoMapper;
	String heartbeat = "ht1";

	@Override
	public Map<String, Object> getHistoricalData(QueryParametersVo queryVo) {
		List<DataAcquisitionVo> dataList = new ArrayList<DataAcquisitionVo>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int total = queryVo.getTotal();
		int limit = queryVo.getLimit();
		int page = queryVo.getPage();
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
			if (state != null && state.equals("1")) {
				SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
						Bytes.toBytes("STATE"), CompareOp.NOT_EQUAL, Bytes.toBytes("5"));
				filterList.addFilter(filter2);
			}
			scan.setFilter(filterList);
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(endRow));
			scan.setMaxVersions();
			if (total == 0) {
				long in = System.currentTimeMillis();
				ResultScanner rs0 = table.getScanner(scan);
				long out = System.currentTimeMillis();
				log.info("数据曲线Hbase查询耗时:" + (out - in) + "ms ");
				total = getTotal(rs0, page);
			}
			long in = System.currentTimeMillis();
			ResultScanner rs = table.getScanner(scan);
			long out = System.currentTimeMillis();
			log.info("数据曲线Hbase查询耗时:" + (out - in) + "ms ");
			Result[] rArray = rs.next(page * limit);
			if (rArray.length > 0) {
				int end = 0;
				int start = (page == 1) ? 0 : (page - 1) * limit;
				if (total > page * limit) {
					end = page * limit;
				} else {
					end = total;
				}
				for (int i = start; i < end; i++) {
					Result r = rArray[i];
					List<Cell> lc = r.listCells();
					DataAcquisitionVo d = newDataAcquisitionVo(lc);
					dataList.add(d);
				}
			}
			resultMap.put("data", dataList);
			resultMap.put("total", total);
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
		List<DataAcquisitionVo> dataList = queryDataFromHbase(queryVo, null);
		Map<String, Object> resultmap = transformState(dataList);
		if (dataList.size() == 0) {
			String equipmentName = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId()).getEquipmentName();
			resultmap.put("equipmentName", equipmentName);
		} else {
			resultmap.put("equipmentName", dataList.get(0).getEquipmentName());
		}
		return resultmap;
	}

	@Override
	public Map<String, Object> realTimeCurve(QueryParametersVo queryVo) throws ParseException {
		List<DataAcquisitionVo> dataList = queryDataFromHbase(queryVo, null);
		Map<String, Object> resultmap = transformState(dataList);
		if (dataList.size() == 0) {
			String equipmentName = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId()).getEquipmentName();
			resultmap.put("equipmentName", equipmentName);
		} else {
			resultmap.put("equipmentName", dataList.get(0).getEquipmentName());
		}
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
		return resultmap;
	}

	private Map<String, Object> transformState(List<DataAcquisitionVo> dataList) {
		Map<String, Object> resultmap = new HashMap<>();
		List<Map<String, List<List<Double>>>> result = new ArrayList<Map<String, List<List<Double>>>>();
		Map<String, List<List<Double>>> map = new TreeMap<>();
		List<Double> allTemperatures = new ArrayList<Double>();
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				DataAcquisitionVo dv = dataList.get(i);
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
		List<DataAcquisitionVo> dataList = queryDataFromHbase(queryVo, null);
		return dataList;
	}

	@Override
	public List<StaticFailures> errorStateStatic(QueryParametersVo queryVo) {
		List<StaticFailures> errorStateStatic = new ArrayList<StaticFailures>();
		queryVo.setStartTime(queryVo.getStartTime() + " 00:00:00");
		queryVo.setEndTime(queryVo.getEndTime() + " 23:59:59");
		int n = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId()).getNumOfCh();
		for (int i = 1; i <= n; i++) {
			StaticFailures sf = new StaticFailures();
			List<DataAcquisitionVo> dataList = queryDataFromHbase(queryVo, "CH" + i);
			if (dataList.size() > 0) {
				sf = staticFailuer(dataList, sf);
			}
			errorStateStatic.add(sf);
		}
		return errorStateStatic;
	}

	int comm = 0;// 458通信故障
	int fiber = 0;// 光纤故障
	int therm = 0;// 测温仪故障
	int overT = 0;// 超温故障
	// 统计一天的故障

	private StaticFailures staticFailuer(List<DataAcquisitionVo> dataList, StaticFailures sf) {
		String equipmentId = dataList.get(0).getEquipmentId();
		String channelNum = dataList.get(0).getChannelNum();
		String equipmentName = dataList.get(0).getEquipmentName();
		String ofp = dataList.get(0).getOpticalFiberPosition();
		String address = dataList.get(0).getAddress();
		for (int i = 0; i < dataList.size() - 1; i++) {
			int state = dataList.get(i).getState();
			int statea = dataList.get(i + 1).getState();
			// 逐条判断前一条数据和下一条数据, 当状态不等且下一条不是正常，就说明有新的故障产生，需要统计
			if (i == 0 && dataList.get(0).getState() != 5) {
				sunError(state);
			}
			if (state != statea && statea != 5) {
				sunError(statea);
			}
		}

		sf.setEquipmentId(equipmentId);
		sf.setEquipmentName(equipmentName);
		sf.setAddress(address);
		sf.setChannelNum(channelNum);
		sf.setOpticalFiberPosition(ofp);
		sf.setCommunicate(comm);
		sf.setFiber(fiber);
		sf.setThermometer(therm);
		sf.setOverTemperature(overT);
		comm = 0;// 458通信故障
		fiber = 0;// 光纤故障
		therm = 0;// 测温仪故障
		overT = 0;// 超温故障
		return sf;
	}

	private void sunError(int statea) {
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

	/**
	 * 通过设备主键，获取指定时间段内的通道温度最值
	 */
	@Override
	public Map<String, Object> getHistoryExtremums(QueryParametersVo queryVo) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		List<DataAcquisitionVo> dataList = queryDataFromHbase(queryVo, null);
		if (dataList.size() == 0) {
			String equipmentName = equipmentInfoMapper.selectByPrimaryKey(queryVo.getEquipmentId()).getEquipmentName();
			resultmap.put("equipmentName", equipmentName);
		} else {
			resultmap.put("equipmentName", dataList.get(0).getEquipmentName());
		}
		List<ChannelExtremum> extremumList = channelExtremums(dataList);
		resultmap.put("extremumList", extremumList);
		return resultmap;
	}

	private List<ChannelExtremum> channelExtremums(List<DataAcquisitionVo> dataList) {
		List<ChannelExtremum> extremumList = new ArrayList<ChannelExtremum>();
		Map<String, List<Float>> map = new TreeMap<>();
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				DataAcquisitionVo dv = dataList.get(i);
				String ch = dv.getChannelNum();
				Float tem = Float.valueOf(dv.getTemperature());
				if (map.containsKey(ch)) {
					map.get(ch).add(tem);
				} else {
					List<Float> ll = new ArrayList<Float>();
					ll.add(tem);
					map.put(ch, ll);
				}
			}

			Iterator<Entry<String, List<Float>>> iterator = map.entrySet().iterator();
			Entry<String, List<Float>> entry;
			while (iterator.hasNext()) {
				entry = iterator.next();
				ChannelExtremum c = new ChannelExtremum();
				Comparator<Float> comparator = (o1, o2) -> o1.compareTo(o2);
				Float max = entry.getValue().stream().max(comparator).get();
				Float min = entry.getValue().stream().min(comparator).get();
				c.setChannel(entry.getKey());
				c.setMax(max);
				c.setMin(min);
				extremumList.add(c);
			}
		}
		return extremumList;
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
		String ch = (d.getChannelNum().length() == 3) ? new StringBuffer(d.getChannelNum()).insert(2, 0).toString()
				: d.getChannelNum();
		try {
			rowkey = CommonUtil.dateToStamp(d.getReceiveTime()) + d.getEquipmentId() + failFlag + d.getState() + ch;
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
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<DataAcquisitionVo> queryDataFromHbase(QueryParametersVo queryVo, String ch) {
		ResultScanner rs = null;
		String tableName = "ns1:hq_equipment_monitor_data";
		Table table = null;
		Connection connection = null;
		String startRow;
		String endRow;
		String equipmentId = queryVo.getEquipmentId();
		String state = queryVo.getState();
		List<DataAcquisitionVo> dataList = new ArrayList<DataAcquisitionVo>();
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
			if (state != null && state.equals("1")) {
				SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
						Bytes.toBytes("STATE"), CompareOp.NOT_EQUAL, Bytes.toBytes("5"));
				filterList.addFilter(filter2);
			}
			if (ch != null) {
				SingleColumnValueFilter filter3 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
						Bytes.toBytes("CHANNEL_NUM"), CompareOp.EQUAL, Bytes.toBytes(ch));
				filterList.addFilter(filter3);
			}
			scan.setFilter(filterList);
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(endRow));
			scan.setMaxVersions();
			long in = System.currentTimeMillis();
			rs = table.getScanner(scan);
			long out = System.currentTimeMillis();
			log.info("数据曲线Hbase查询耗时:" + (out - in) + "ms ");
			for (Result r : rs) {
				List<Cell> lc = r.listCells();
				DataAcquisitionVo d = newDataAcquisitionVo(lc);
				dataList.add(d);
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
		return dataList;
	}

	private DataAcquisitionVo newDataAcquisitionVo(List<Cell> lc) {
		DataAcquisitionVo d = new DataAcquisitionVo();
		d.setAddress(Bytes.toString(lc.get(0).getValueArray()));
		d.setChannelNum(Bytes.toString(lc.get(1).getValueArray()));
		d.setEquipmentId(Bytes.toString(lc.get(3).getValueArray()));
		d.setEquipmentName(Bytes.toString(lc.get(4).getValueArray()));
		d.setMessage(Bytes.toString(lc.get(5).getValueArray()));
		d.setOpticalFiberPosition(Bytes.toString(lc.get(6).getValueArray()));
		d.setPd(Bytes.toString(lc.get(7).getValueArray()));
		d.setReceiveTime(Bytes.toString(lc.get(8).getValueArray()));
		d.setState(Integer.valueOf(Bytes.toString(lc.get(9).getValueArray())));
		d.setTemperature(Bytes.toString(lc.get(11).getValueArray()));
		d.setUv(Bytes.toString(lc.get(12).getValueArray()));
		return d;
	}

	@Override
	public List<DataAcquisitionVo> parseReceivedData(String inputString, String url) throws IOException {
		List<DataAcquisitionVo> realTimeDateList = new ArrayList<DataAcquisitionVo>();
		String frameStru;
		// 获取返回祯的CRC校验
		String rameStru = inputString.substring(inputString.length() - 4);
		// 获取返回祯的有效数据（即除去CRC校验的剩余祯）
		String validInput = inputString.substring(0, inputString.length() - 4);
		// 对有效数据进行CRC校验码验证
		String crc = CommonUtil.getCRC(validInput).replace(" ", "").toLowerCase();
		// 判断返回祯的CRC校验码是否为真
		if (crc.equals(rameStru)) {
			Map<String, String> param = new HashMap<>();
			frameStru = Integer.valueOf(inputString.substring(0, 2), 16).toString();
			// 通道数量
			int num = Integer.valueOf(inputString.substring(4, 6), 16) / 4;
			log.info("客户端返回数据获取的通道数量为：" + num + "----祯是：" + frameStru);
			// 去掉前六个标识性字符，真正的有效字符是从第七个开始
			inputString = inputString.substring(6);
			param.put("frameStru", frameStru);
			param.put("heartbeatId", heartbeat);
			// 通过心跳包ID和设备的帧结构获取唯一设备信息
			EquipmentInfo e = equipmentService.selectByHbid(param);
			if (e != null) {
				String equipmentId = e.getEquipmentId();
				String equipmentName = e.getEquipmentName();
				String channelTem = e.getChannelTem();
				channelTem = channelTem.substring(2, channelTem.length() - 2);
				String[] arr = channelTem.split("\\],\\[");
				List<String> cnl = new ArrayList<String>();// 通道list
				List<String> opticall = new ArrayList<String>();// 光纤位置list
				List<String> maxl = new ArrayList<String>();// 最大值list
				List<String> minl = new ArrayList<String>();// 最小值list
				if (arr.length == e.getNumOfCh()) {
					for (int i = 0; i < e.getNumOfCh(); i++) {
						String[] ta = arr[i].split(",");
						String cn = ta[0].substring(1, ta[0].length() - 1);
						String optical = ta[1].substring(1, ta[1].length() - 1);
						String max = ta[2].substring(1, ta[2].length() - 1);
						String min = ta[3].substring(1, ta[3].length() - 1);
						cnl.add(cn);
						opticall.add(optical);
						maxl.add(max);
						minl.add(min);
					}
				}
				// 测温仪的公共信息
				String receiveTime = CommonUtil.getSimpleFormatTimestamp();
				// 测温仪 每个通道的信息
				for (int i = 0; i < num; i++) {
					DataAcquisitionVo d = new DataAcquisitionVo();
					d.setEquipmentId(equipmentId);
					d.setEquipmentName(equipmentName);
					d.setAddress(frameStru);
					d.setReceiveTime(receiveTime);
					d.setDutyPerson(e.getUserName());
					d.setTel(e.getTel());
					d.setNumOfCh(e.getNumOfCh());
					Float temp = Integer.valueOf(inputString.substring(i * 8, i * 8 + 4), 16) / 10F;
					Integer pd = Integer.valueOf(inputString.substring(i * 8 + 4, i * 8 + 6), 16);
					Integer uv = Integer.valueOf(inputString.substring(i * 8 + 6, i * 8 + 8), 16);
					d.setChannelNum(cnl.get(i));
					d.setOpticalFiberPosition(opticall.get(i));
					d.setTemperature(temp.toString());
					d.setPd(pd.toString());
					d.setUv(uv.toString());
					switch (temp.toString()) {
					case "3000.0":
						d.setState(2);
						d.setTemperature("3000");
						d.setMessage("光纤故障");
						break;
					case "6116.6":
						d.setState(1);
						d.setTemperature("-437");
						d.setMessage("传感器模块故障");
						break;
					case "2999.9":
						d.setState(3);
						d.setTemperature("2999");
						d.setMessage("系统调整中");
						break;
					default:
						d.setState(5);
						d.setMessage("正常");
						break;
					}
					if (temp != 3000.0F && temp != -6116.6F && temp != 2999.9F && temp != 6548.3F
							&& (temp < Float.valueOf(minl.get(i)) || temp > Float.valueOf(maxl.get(i)))) {
						d.setState(9);
					}
					// 往SQL server中插入数据，为了找问题方便看
					dataAcquisitionVoMapper.insertSQL(d);
					// 往Hbase中插入数据
					insertHbase(d);
					realTimeDateList.add(d);
				}

			}
		}
		return realTimeDateList;
	}

}
