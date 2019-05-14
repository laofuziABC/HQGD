package com.hqgd.pms.controller.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import javax.annotation.Resource;

import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateTableTimer extends TimerTask {
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	public UpdateTableTimer() {
		this.dataAcquisitionVoMapper = SpringContextUtil.getBean(DataAcquisitionVoMapper.class);
	}

	@Override
	public void run() {
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1460);
		String lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
		Map<String, Object> param = new HashMap<>();
		// 判断当前时间和登录时间的差额,如果小于一天就把临时表1里的数据放进临时表2,如果大于一天就把临时表2里的最旧一条删除，再插入最新的一条
		log.info("更新表执行中！");
		for (int i = 1; i <= 4; i++) {
			param.put("lastTime", lastTime);
			param.put("table", "hq_equipment_monitor_data_r" + i);
			dataAcquisitionVoMapper.deleter(param);
		}
		for (int i = 1; i <= 4; i++) {
			param.put("tabler", "hq_equipment_monitor_data_r" + i);
			param.put("tablef", "hq_equipment_monitor_data_f" + i);
			dataAcquisitionVoMapper.insertt(param);

		}
	}

}
