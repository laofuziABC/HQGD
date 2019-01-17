package com.hqgd.pms.service.images;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hqgd.pms.domain.ImageInfo;

public interface IImagesService {

	Map<String, Object> delete(String imagesId);

	ImageInfo select(String imagesId);

	List<ImageInfo> selectAll();

	Map<String, Object> add(MultipartFile[] file, String group);

}
