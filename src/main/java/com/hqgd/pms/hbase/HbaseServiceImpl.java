package com.hqgd.pms.hbase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
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

import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;

@Service
public class HbaseServiceImpl {
	@Autowired
	private HbaseConfig con;

	/**
	 * 根据tableName，rowkey，family coluem 查询单个数据
	 */
	public String getValue(String tableName, String rowkey, String family, String column) {
		Table table = null;
		Connection connection = null;
		String res = "";
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || StringUtils.isBlank(rowkey)
				|| StringUtils.isBlank(column)) {
			return null;
		}
		try {
			connection = ConnectionFactory.createConnection(con.configuration());
			table = connection.getTable(TableName.valueOf(tableName));
			Get g = new Get(rowkey.getBytes());
			g.addColumn(family.getBytes(), column.getBytes());
			Result result = table.get(g);
			List<Cell> ceList = result.listCells();
			if (ceList != null && ceList.size() > 0) {
				for (Cell cell : ceList) {
					res = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
					System.out.println(res);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 根据tableName，rowkey，family coluem 查询多个数据
	 * 
	 * @return
	 */
	public String scanValue(String endtime,String starttime) throws IOException {
		String tableName = "ns1:hq_equipment_monitor_data";
		String rowkey = "row001";
		String family = "f1";
		String column = "TEMPERATURE";

		Table table = null;
		Connection connection = null;
		String res = "";
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || StringUtils.isBlank(rowkey)
				|| StringUtils.isBlank(column)) {
			return null;
		}
		try {

			connection = ConnectionFactory.createConnection(con.configuration());
			table = connection.getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();

			FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
					Bytes.toBytes("RECEIVE_TIME"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(starttime));
			SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
					Bytes.toBytes("RECEIVE_TIME"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(endtime));
			SingleColumnValueFilter filter3 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
					Bytes.toBytes("EQUIPMENT_ID"), CompareOp.EQUAL, Bytes.toBytes("0x000008"));
			filterList.addFilter(filter1);
			filterList.addFilter(filter2);
			filterList.addFilter(filter3);
			scan.setFilter(filterList);
			long startTime = System.currentTimeMillis();
			ResultScanner rs = table.getScanner(scan);
			long endTime = System.currentTimeMillis();
			System.out.println("总耗时:" + (endTime - startTime) + "ms ");
			for (Result r : rs) {
				for (Cell cell : r.listCells()) {
					System.out.println(Bytes.toString(cell.getRow()) + " Familiy:Quilifier : "
							+ Bytes.toString(cell.getFamily()) + ":" + Bytes.toString(cell.getQualifier()) + " Value : "
							+ Bytes.toString(cell.getValue()) + " Time : " + cell.getTimestamp());
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	public String test(String endtime) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", "0x000009");
		param.put("startTime", "2019-02-18 18:10:53");
		param.put("endTime", endtime);
		// param.put("state", "5");
		param.put("table", "hq_equipment_monitor_data");
		long startTime = System.currentTimeMillis();
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionVoMapper.selectCurveById(param);
		long endTime = System.currentTimeMillis();
		System.out.println(
				"总耗时:" + (endTime - startTime) + "ms     historicalDataList的size：" + historicalDataList.size());
		return null;

	}

	public String put() throws IOException {
		
		return null;
	}

}