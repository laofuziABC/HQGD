package com.hqgd.pms.dao.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hqgd.pms.dao.user.IUserDao;
import com.hqgd.pms.domain.User;

@Repository
public class UserDao implements IUserDao{
	
	@Autowired
    SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public User getUserByUserName(String userName) {
		return sqlSessionTemplate.selectOne("findUserByUserName", userName);
	}
	@Override
	public void updatePassword(String userName, String newPassword) {

		Map<String, String> param = new HashMap<>();
		param.put("username", userName);
		param.put("newPassword", newPassword);
		sqlSessionTemplate.update("updatePassword", param);
	
	}
	@Override
	public List<User> selectAll() {
		return sqlSessionTemplate.selectList("selectAll");
	}
	
}
