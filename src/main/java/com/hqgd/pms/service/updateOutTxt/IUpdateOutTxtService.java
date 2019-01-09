package com.hqgd.pms.service.updateOutTxt;

import java.util.ArrayList;

import com.hqgd.pms.domain.ChannelTemp;

public interface IUpdateOutTxtService {

	Boolean insertBatch(ArrayList<ChannelTemp> channelTempList);

}
