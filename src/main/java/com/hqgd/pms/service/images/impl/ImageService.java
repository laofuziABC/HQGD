package com.hqgd.pms.service.images.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.image.ImageInfoMapper;
import com.hqgd.pms.domain.ImageInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.images.IImagesService;

@Service
public class ImageService implements IImagesService {
	@Resource
	private ImageInfoMapper imageInfoMapper;
	@Override
	public Map<String, Object> add(ImageInfo imagesInfo, User loginUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> delete(String imagesId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageInfo select(String imagesId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ImageInfo> selectAll() {
		List<ImageInfo> imagesInfoList = imageInfoMapper.selectAll();
		return imagesInfoList;
	}

	@Override
	public String execRecordExport(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> update(ImageInfo imagesInfo, User loginUser) {
		// TODO Auto-generated method stub
		return null;
	}
}
