package com.hqgd.pms.service.user;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.User;

public interface IUserService {

	User findUserByUserName(String username);

	User getUserById(int userId);

	void updatePassword(String userName, String newPassword);

	Map<String, Object> add(User user, User userLog);

	Map<String, Object> delete(String userId);

	Map<String, Object> update(User user, User userLog);

	User select(String userId);

	List<User> selectAll();

	Boolean isSystemUser(Integer id);

}
