package com.tarena.cloudnote.service;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarena.cloudnote.dao.UserMybatisDao;
import com.tarena.cloudnote.entity.User;
import com.tarena.cloudnote.util.Md5Utils;
import com.tarena.cloudnote.util.UUIDUtil;

@Service
@Transactional
public class UserService {

	@Resource(name = "userMybatisDao")
	private UserMybatisDao userMybatisDao;
	
	public User validateUserAndPwd(String userName, String userPwd) {
		User user = new User();
		if(userName!=null){
			user = userMybatisDao.getUserByName(userName);
			if(user!=null && user.getCnUserPassword().equals(userPwd)){
				return user;
			}
		}
		return null;
 	} 
	public void createUser(User user) {
		user.setCnUserId(UUIDUtil.getUId());
		user.setCnUserPassword(Md5Utils.md5(user.getCnUserPassword()));
		userMybatisDao.createUser(user);
	}
}
