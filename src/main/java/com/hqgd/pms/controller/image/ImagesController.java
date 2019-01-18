package com.hqgd.pms.controller.image;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.ImageInfo;
import com.hqgd.pms.service.images.IImagesService;
import com.hqgd.pms.service.user.IUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚绒 图片上传增删改查
 */
@Controller
@Scope("request")
@Slf4j
@RequestMapping("images")
public class ImagesController {
	@Autowired
	IImagesService imagesService;
	@Autowired
	IUserService userService;

	@RequestMapping(value = "/add")
	public void add(@RequestParam("files[]") MultipartFile[] file,String group,Model model,HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("添加图例开始");
		Map<String, Object> resultMap = imagesService.add(file,group);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("添加图例结束");
	}

	@RequestMapping(value = "/delete")
	public void delete(Model model, String imageName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("删除设备开始");
		Map<String, Object> resultMap = imagesService.delete(imageName);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("删除设备结束");
	}

	@RequestMapping(value = "/select")
	public void select(Model model, String imageName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("查询设备开始");
		ImageInfo imagesInfo = imagesService.select(imageName);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000004");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", imagesInfo == null ? "null" : imagesInfo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询设备结束");
	}

	@RequestMapping(value = "/selectAll")
	@ResponseBody
	public void selectAll(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("查询所有图例开始");
		List<ImageInfo> imagesList = imagesService.selectAll();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000005");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", imagesList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询所有图例结束");
	}

}
