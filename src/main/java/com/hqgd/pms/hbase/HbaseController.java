package com.hqgd.pms.hbase;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.hbase.HbaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚绒 设备管理，增删改查
 */
@Controller
@Slf4j
@RequestMapping("hbase")
public class HbaseController {
	@Autowired
	private HbaseServiceImpl hbaseServiceImpl;

	@RequestMapping(value = "/getValue")
	public void getValue(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("开始");
		String tableName = "ns1:t1";
		String rowkey = "row001";
		String family = "f1";
		String column = "name";
		String result = hbaseServiceImpl.getValue(tableName, rowkey, family, column);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(result));
		log.info("结束");
	}

	@RequestMapping(value = "/scanValue")
	public void scanValue(HttpServletRequest request, HttpServletResponse response, String endtime) throws IOException {
		log.info("开始");
		String result = hbaseServiceImpl.scanValue(endtime);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(result));
		log.info("结束");
	}

	@RequestMapping(value = "/test")
	public void test(HttpServletRequest request, HttpServletResponse response, String endtime) throws IOException {
		log.info("开始");
		String result = hbaseServiceImpl.test(endtime);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(result));
		log.info("结束");
	}

}