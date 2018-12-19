package com.hqgd.pms.service.images;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.ImagesInfo;
import com.hqgd.pms.domain.User;

public interface IImagesService {

	Map<String, Object> add(ImagesInfo imagesInfo, User loginUser);

	Map<String, Object> delete(String imagesId);

	ImagesInfo select(String imagesId);

	List<ImagesInfo> selectAll();

	String execRecordExport(String path);

	Map<String, Object> update(ImagesInfo imagesInfo, User loginUser);

}
