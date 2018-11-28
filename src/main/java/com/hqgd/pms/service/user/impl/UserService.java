package com.hqgd.pms.service.user.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.user.UserMapper;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

/**
 * 描述： 作者：姚绒 日期：2018年11月5日 上午9:43:08
 *
 */
@Service
public class UserService implements IUserService {

	@Resource
	private UserMapper userMapper;

	public void updatePassword(String userName, String newPassword) {
		Map<String, String> param = new HashMap<>();
		param.put("username", userName);
		param.put("newPassword", newPassword);
		userMapper.updatePassword(param);
	}

	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public User findUserByUserName(String userName) {
		return userMapper.findUserByUserName(userName);
	}

	@Override
	public Map<String, Object> add(User user, User loginUser) {
		Boolean result = false;
		String message = "";
		User userfind = userMapper.findUserByUserName(user.getUserName());
		if (userfind == null) {
			user.setUpdater(loginUser.getUserName());
			user.setCreator(loginUser.getUserName());
			user.setUpdateTime(new Date());
			user.setCreateTime(new Date());
			user.setIsdel("N");
			int i = userMapper.insert(user);
			result = (i == 0) ? false : true;
			message = "添加用户成功！";
		} else {
			message = "该用户名已被注册！";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("success", result);
		map.put("message", message);
		return map;
	}

	@Override
	public Map<String, Object> delete(String id) {
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.deleteByPrimaryKey(Integer.valueOf(id));
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

	@Override
	public Map<String, Object> update(User user, User loginUser) {
		user.setUpdater(loginUser.getUserName());
		user.setUpdateTime(new Date());
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.updateByPrimaryKeySelective(user);
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

	@Override
	public User select(String id) {
		User user = userMapper.selectByPrimaryKey(Integer.valueOf(id));
		return user;
	}

	@Override
	public List<User> selectAll() {
		return userMapper.selectAll();
	}

	@Override
	public Boolean isSystemUser(Integer id) {
		User user = userMapper.selectUserRoleType(id);
		if (user != null && user.getId() == 0) {
			return true;
		} else {
			return false;
		}
	}

}
