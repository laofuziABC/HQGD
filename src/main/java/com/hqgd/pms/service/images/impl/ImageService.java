package com.hqgd.pms.service.images.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.hqgd.pms.dao.image.ImageInfoMapper;
import com.hqgd.pms.domain.ImageInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.images.IImagesService;

@Service
public class ImageService implements IImagesService {
	@Resource
	private ImageInfoMapper imageInfoMapper;
	File tempPathFile;

	@Override
	public Map<String, Object> add(HttpServletRequest request) {
		User loginUser = (User) request.getSession(true).getAttribute("user");
		/*
		 * String group = request.getParameter("group"); String imageList =
		 * request.getParameter("imageList"); request.getParameter("file");
		 */
		/*
		 * try { DiskFileItemFactory factory = new DiskFileItemFactory();
		 * factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
		 * factory.setRepository(tempPathFile);// 设置缓冲区目录 ServletFileUpload upload = new
		 * ServletFileUpload(factory); upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB
		 * List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
		 * Iterator<FileItem> i = items.iterator(); String uploadPath = "D:\\temp";
		 * while (i.hasNext()) { FileItem fi = (FileItem) i.next(); String fileName =
		 * fi.getName(); if (fileName != null) { File fullFile = new File(new
		 * String(fi.getName().getBytes(), "utf-8")); // 解决文件名乱码问题
		 * 
		 * File savedFile = new File(uploadPath, fullFile.getName());
		 * fi.write(savedFile); } } System.out.print("upload succeed"); } catch
		 * (Exception e) {
		 * 
		 * }
		 */
		try {
			StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
			Iterator<String> iterator = req.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile file = req.getFile(iterator.next());
				String fileName = file.getOriginalFilename();
				long fileSize = file.getSize();
				String group = fileName.substring(0, 1);
				fileName = fileName.substring(1, fileName.length() - 1);
				String path ="";
				if (group.equals("1")) {
					 path = "/static/vince/images/graphicDesign/advanced/" + fileName;
				}else if (group.equals("3")) {
					 path = "/static/vince/images/graphicDesign/base/" + fileName;
				}else {
					path = "/static/vince/images/graphicDesign/customize/" + fileName;
				}
				file.transferTo(new File(path));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
