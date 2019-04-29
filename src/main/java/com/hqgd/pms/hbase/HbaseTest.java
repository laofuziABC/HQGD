package com.hqgd.pms.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class HbaseTest {
	// 添加数据
	@Test
	public void testPut() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		// 指定zk的地址
		conf.set("hbase.zookeeper.quorum", "uplooking03:2181,uplooking04:2181,uplooking05:2181");
		Connection conn = ConnectionFactory.createConnection(conf);
		Table table = conn.getTable(TableName.valueOf("ns1:t1"));
		Put put = new Put(Bytes.toBytes("row001"));
		put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("admin02"));
		table.put(put);
	}

	// 删除数据
	@Test
	public void testDelete() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		// 指定zk的地址
		conf.set("hbase.zookeeper.quorum", "uplooking03:2181,uplooking04:2181,uplooking05:2181");
		Connection conn = ConnectionFactory.createConnection(conf);
		Table table = conn.getTable(TableName.valueOf("ns1:t1"));
		Delete delete = new Delete(Bytes.toBytes("row001"));
		table.delete(delete);
	}

	// 查询数据
	@Test
	public void testGet() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		// 指定zk的地址
		conf.set("hbase.zookeeper.quorum", "uplooking03:2181,uplooking04:2181,uplooking05:2181");
		Connection conn = ConnectionFactory.createConnection(conf);
		Table table = conn.getTable(TableName.valueOf("ns1:t1"));
		Get get = new Get(Bytes.toBytes("row001"));
		long startTime = System.currentTimeMillis();
		Result result = table.get(get);
		long endTime = System.currentTimeMillis();
		String s = Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name")));
		System.out.println(s);
		System.out.println("总耗时:" + (endTime - startTime) + "ms");
	}

	// 查询数据
	@Test
	public void testScan() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		// 指定zk的地址
		conf.set("hbase.zookeeper.quorum", "uplooking03:2181,uplooking04:2181,uplooking05:2181");
		Connection conn = ConnectionFactory.createConnection(conf);
		Table table = conn.getTable(TableName.valueOf("ns1:hq_equipment_monitor_data"));
		Scan scan = new Scan();
		byte[] cf = Bytes.toBytes("f1");
		byte[] column = Bytes.toBytes("RECEIVE_TIME");
		FilterList filters = new FilterList();
		Filter filter1 = new ValueFilter(CompareFilter.CompareOp.GREATER,
				new BinaryPrefixComparator(Bytes.toBytes("2019-03-01 00:00:00")));
		Filter filter2 = new ValueFilter(CompareFilter.CompareOp.LESS,
				new BinaryPrefixComparator(Bytes.toBytes("2019-04-01 00:00:00")));
		filters.addFilter(filter1);
		filters.addFilter(filter2);
		scan.setFilter(filters);
		long startTime = System.currentTimeMillis();
		// 获取包含多行数据的对象
		ResultScanner resultScanner = table.getScanner(scan);
		long endTime = System.currentTimeMillis();
		System.out.println("总耗时:" + (endTime - startTime) + "ms");
		for (Result result : resultScanner) {
			System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("RECEIVE_TIME"))));
		}
	}
}