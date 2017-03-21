package com.myshell.service;

import com.myshell.daoimpl.UserDaoImpl;
import com.myshell.model.User;

public class UserService {
	static UserService userService;
	UserDaoImpl userDaoImpl;
	public static UserService getInstance(){
		if(userService==null){
			userService=new UserService();
		}
		return userService;
	}
	public UserService(){
		userDaoImpl=UserDaoImpl.getInstance();
	}
	public void add(User user){
		userDaoImpl.add(user);
	}
	public Boolean isExistName(User user){
		return userDaoImpl.isExistName(user);
	}
	public Boolean isRightPasswd(User user){
		return userDaoImpl.isRightPasswd(user);
	}
}
