package com.hqgd.pms.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.hqgd.pms.domain.User;

public interface IUserDao {

	User getUserByUserName(String userName);

	void updatePassword(String userName, String newPassword);

	List<User> selectAll();

}