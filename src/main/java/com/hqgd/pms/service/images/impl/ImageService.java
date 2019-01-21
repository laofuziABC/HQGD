package com.hqgd.pms.service.images.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.image.ImageInfoMapper;
import com.hqgd.pms.domain.ImageInfo;
import com.hqgd.pms.service.images.IImagesService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ImageService implements IImagesService {
	@Resource
	private ImageInfoMapper imageInfoMapper;
	File tempPathFile;
	private static final String updatepath = "D:/工具软件/static/vince/images/graphicDesign";

	@Override
	public Map<String, Object> add(MultipartFile[] MultipartFile, String group) {
		Map<String, Object> resultMap = new HashMap<>();
		String path = "";
		try {
			for (int i = 0; i < MultipartFile.length; i++) {
				MultipartFile file = MultipartFile[i];
				String fileName = file.getOriginalFilename();
				long fileSize = file.getSize();
				ImageInfo ImageFind = imageInfoMapper.selectByPrimaryKey(fileName);
				if (ImageFind != null) {
					resultMap.put("message", "该图例名称已经存在");
					resultMap.put("success", false);
					return resultMap;
				}
				ImageInfo imageInfo = new ImageInfo();
				if (group.equals("1")) {
					imageInfo.setGroups("高级");
					path = updatepath + "/advanced/" + fileName;
				} else if (group.equals("3")) {
					imageInfo.setGroups("基本");
					path = updatepath + "/base/" + fileName;
				} else {
					imageInfo.setGroups("自定义");
					path = updatepath + "/customize/" + fileName;
				}
				log.info("path:"+path);
				File f = new File(path);
				if (!f.getParentFile().exists())
					f.getParentFile().mkdirs();
				file.transferTo(f);
				imageInfo.setAuthor("");
				imageInfo.setName(fileName);
				/* imageInfo.setPath(path); */
				imageInfo.setPath(path.substring(path.indexOf("static") + 7, path.length()));
				imageInfo.setSize(Double.valueOf(fileSize));
				imageInfo.setIsdel(0);
				imageInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
				int j = imageInfoMapper.insert(imageInfo);
				boolean result = (j == 0) ? false : true;
				resultMap.put("message", (result) ? "添加设备成功" : "添加设备失败");
				resultMap.put("success", result);
				resultMap.put("resultCode", "00000003");
				resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resultMap;

	}

	@Override
	public Map<String, Object> delete(String imagesName) {
		Map<String, Object> resultMap = new HashMap<>();
		int i = imageInfoMapper.deleteByPrimaryKey(imagesName);
		Boolean result = (i == 0) ? false : true;
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000002");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", (result) ? "添加图例成功" : "添加图例失败");
		return resultMap;
	}

	@Override
	public ImageInfo select(String imagesName) {
		ImageInfo imageInfo = imageInfoMapper.selectByPrimaryKey(imagesName);
		return imageInfo;
	}

	@Override
	public List<ImageInfo> selectAll() {
		List<ImageInfo> imagesInfoList = imageInfoMapper.selectAll();
		return imagesInfoList;
	}

}
