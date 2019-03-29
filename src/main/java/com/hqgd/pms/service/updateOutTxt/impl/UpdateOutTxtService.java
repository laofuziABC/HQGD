package com.hqgd.pms.service.updateOutTxt.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.updateOutTxt.ChannelTempMapper;
import com.hqgd.pms.domain.ChannelTemp;
import com.hqgd.pms.service.updateOutTxt.IUpdateOutTxtService;

@Service
public class UpdateOutTxtService implements IUpdateOutTxtService{
	@Resource
	private ChannelTempMapper channelTempMapper;
	@Override
	public Boolean insertBatch(ArrayList<ChannelTemp> channelTempList) {
		for(ChannelTemp c:channelTempList) {
			channelTempMapper.insert(c);
		}
		return true;
	}

}
