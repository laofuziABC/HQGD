package com.hqgd.pms.controller.updateOutTxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqgd.pms.domain.ChannelTemp;
import com.hqgd.pms.service.updateOutTxt.IUpdateOutTxtService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * @author 姚绒 批量上传文件
 */
@Controller
@Scope("request")
@RequestMapping("update")
public class UpdateOutTxtController {

	@Autowired
	IUpdateOutTxtService updateOutTxtService;

	@RequestMapping(value = "/channelTempText")
	public void importTxt() throws IOException, ParseException {
		String encoding = "GBK";
		ArrayList<ChannelTemp> channelTempList = new ArrayList<>();
		String dir = "E:\\test";
		File[] files = new File(dir).listFiles();
		for (File file : files) {
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				importFile(file, encoding, channelTempList); // 将文件中的数据读取出来，并存放进集合中
			}
			updateOutTxtService.insertBatch(channelTempList); // 将集合中的数据批量入库
			channelTempList.clear();
		}
	}

	/** 读取数据，存入集合中 */
	public static void importFile(File file, String encoding, ArrayList<ChannelTemp> channelTempList)
			throws IOException, ParseException {
		try {
			InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-16");
			BufferedReader bf = new BufferedReader(inputReader);
			String str;
			while ((str = bf.readLine()) != null) {
				String[] lineArr = str.split("  ");
				if (lineArr.length > 1) {
					ChannelTemp channelTemp = new ChannelTemp();
					if(lineArr[0].substring(1, lineArr[0].length()-1).length()<3) {
						channelTemp.setChannelNum(lineArr[0].substring(0, lineArr[0].length()-1));
					}else {
						channelTemp.setChannelNum(lineArr[0].substring(1, lineArr[0].length()-1));
					}
					channelTemp.setTem(lineArr[1].substring(1, lineArr[1].length()-1));
					channelTemp.setReceiveTime(lineArr[2].substring(1, lineArr[2].length()-1));
					channelTempList.add(channelTemp);
				}
			}
			bf.close();
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
