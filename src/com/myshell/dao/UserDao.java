package com.myshell.dao;

import com.myshell.model.User;

public interface UserDao {
	public Boolean isExistName(User user);
	public Boolean isRightPasswd(User user);
	public void add(User user);
}
