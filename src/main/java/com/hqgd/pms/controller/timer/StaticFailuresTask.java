package com.hqgd.pms.controller.timer;

import javax.annotation.Resource;

import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.service.dataAcquisition.impl.DataAcquisitionService;

/**
 * 描述： 作者：姚绒 日期：2019年3月14日 下午4:41:43
 *
 */
public class StaticFailuresTask implements Runnable  {
	@Resource
	private DataAcquisitionService dataAcquisitionService;
	private String startTime;

	public StaticFailuresTask(String startTime) {
		this.dataAcquisitionService = SpringContextUtil.getBean(DataAcquisitionService.class);
		this.startTime = startTime;
	}

	@Override
	public void run() {
		dataAcquisitionService.run(startTime);
	}

}
