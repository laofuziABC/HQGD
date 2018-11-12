package com.hqgd.pms.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.user.IUserDao;
import com.hqgd.pms.dao.user.UserMapper;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

/**
 * 描述： 作者：姚绒 日期：2018年11月5日 上午9:43:08
 *
 */
@Service
public class UserService implements IUserService {

	@Autowired
	IUserDao userDao;

	@Resource
    private UserMapper userMapper;


	public void updatePassword(String userName, String newPassword) {
		userDao.updatePassword(userName, newPassword);
	}

	
	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public User findUserByUserName(String userName) {
		return userDao.getUserByUserName(userName);
	}


	@Override
	public Map<String, Object> add(User user) {
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.insert(user);
		Boolean result = (i == 0) ? false : true;
		map.put("result", result);
		return map;
	}


	@Override
	public Map<String, Object> delete(String userId) {
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.deleteByPrimaryKey(Integer.valueOf(userId));
		Boolean result = (i == 0) ? false : true;
		map.put("result", result);
		return map;
	}


	@Override
	public Map<String, Object> update(User user) {
		Map<String, Object> map = new HashMap<>();
		int i = userMapper.updateByPrimaryKeySelective(user);
		Boolean result = (i == 0) ? false : true;
		map.put("result", result);
		return map;
	}


	@Override
	public User select(String userId) {
		User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
		return user;
	}


	@Override
	public List<User> selectAll() {
		return  userDao.selectAll();
	}


}
