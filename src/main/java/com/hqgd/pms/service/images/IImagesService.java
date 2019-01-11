package com.hqgd.pms.service.images;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hqgd.pms.domain.ImageInfo;
import com.hqgd.pms.domain.User;

public interface IImagesService {

	Map<String, Object> delete(String imagesId);

	ImageInfo select(String imagesId);

	List<ImageInfo> selectAll();

	String execRecordExport(String path);

	Map<String, Object> update(ImageInfo imagesInfo, User loginUser);

	Map<String, Object> add(HttpServletRequest request);

}
