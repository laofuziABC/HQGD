package com.hqgd.pms.dao.user;

import java.util.List;

import com.hqgd.pms.domain.User;

public interface IUserDao {

	User getUserByUserName(String userName);

	void updatePassword(String userName, String newPassword);

	List<User> selectAll();

}