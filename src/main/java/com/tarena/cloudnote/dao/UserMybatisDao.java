package com.tarena.cloudnote.dao;

import java.util.Map;

import com.tarena.cloudnote.entity.User;

@MyBatisRepository
public interface UserMybatisDao {
//..
	public void createUser(User user);

	public User getUserByName(String userName);

	public void updatePassword(User user);
}
