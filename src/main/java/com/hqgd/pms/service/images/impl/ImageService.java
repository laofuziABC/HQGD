package com.hqgd.pms.service.images.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
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
	private static final String updatepath = "static/vince/images/graphicDesign";

	@Override
	public Map<String, Object> add(MultipartFile[] MultipartFile, String group) {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取跟目录
		File classpath = null;
		try {
			classpath = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!classpath.exists())
			classpath = new File("");
		log.info("path:" + classpath.getAbsolutePath());

		// 如果上传目录为/static/images/upload/，则可以如下获取：
		File upload = new File(classpath.getAbsolutePath(), updatepath);
		if (!upload.exists())
			upload.mkdirs();
		log.info("upload url:" + upload.getAbsolutePath());
		// 在开发测试模式时，得到的地址为：{项目跟目录}/target/static/vince/images/graphicDesign/
		// 在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/vince/images/graphicDesign/
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
				String path = upload.getAbsolutePath();
				ImageInfo imageInfo = new ImageInfo();
				if (group.equals("1")) {
					imageInfo.setGroups("高级");
					path = path + "/advanced/" + fileName;
				} else if (group.equals("3")) {
					imageInfo.setGroups("基本");
					path = path + "/base/" + fileName;
				} else {
					imageInfo.setGroups("自定义");
					path = path + "/customize/" + fileName;
				}
				file.transferTo(new File(path));
				imageInfo.setAuthor("");
				imageInfo.setName(fileName);
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
