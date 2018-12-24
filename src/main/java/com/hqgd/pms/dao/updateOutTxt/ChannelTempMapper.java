package com.hqgd.pms.dao.updateOutTxt;

import com.hqgd.pms.domain.ChannelTemp;

public interface ChannelTempMapper {
    int insert(ChannelTemp record);

    int insertSelective(ChannelTemp record);
}