package com.hqgd.pms.dao.image;

import java.util.List;

import com.hqgd.pms.domain.ImageInfo;

public interface ImageInfoMapper {
    int deleteByPrimaryKey(String name);

    int insert(ImageInfo record);

    int insertSelective(ImageInfo record);

    ImageInfo selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(ImageInfo record);

    int updateByPrimaryKey(ImageInfo record);

	List<ImageInfo> selectAll();
}