package com.hqgd.pms.controller.image;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.ImagesInfo;
import com.hqgd.pms.domain.User;
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
	public void add(Model model, ImagesInfo imagesInfo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("添加设备开始");
		User loginUser = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> resultMap = imagesService.add(imagesInfo, loginUser);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("添加设备结束");
	}

	@RequestMapping(value = "/delete")
	public void delete(Model model, String imagesId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("删除设备开始");
		Map<String, Object> resultMap = imagesService.delete(imagesId);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("删除设备结束");
	}

	@RequestMapping(value = "/update")
	public void update(Model model, ImagesInfo imagesInfo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("更新设备开始");
		User loginUser = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = imagesService.update(imagesInfo, loginUser);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("更新设备结束");
	}

	@RequestMapping(value = "/select")
	public void select(Model model, String imagesId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("查询设备开始");
		ImagesInfo imagesInfo = imagesService.select(imagesId);
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
		log.info("查询所有设备开始");
		List<ImagesInfo> imagesList = imagesService.selectAll();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000005");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", imagesList);
		resultMap.put("total", imagesList.size());
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询所有设备结束");
	}

	@RequestMapping("/recordExport")
	public void recordExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("path");
		String classPath = imagesService.execRecordExport(path);
		try {
			try {
				path = new String(classPath.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			// response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + path);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", Boolean.TRUE.toString());
			resultMap.put("resultCode", "00000000");
			resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
			resultMap.put("message", "查询历史数据成功");
			resultMap.put("data", path);
			response.getWriter().write(new Gson().toJson(resultMap));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
