package com.hqgd.pms.dao.user;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectUserRoleType(Integer id);
    
	User selectByUserName(String userName);

	List<User> selectAll();

	int initUserPassword(String id);

	void updatePassword(String id, String newPassword);

	void resetPassword(Map<String, Object> map);

	User selectByPhUn(Map<String, Object> map);

	int reUse(Integer id, User user);

}