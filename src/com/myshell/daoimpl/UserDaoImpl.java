package com.myshell.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.myshell.dao.UserDao;
import com.myshell.model.User;
import com.myshell.util.ConnDb;

public class UserDaoImpl implements UserDao{
	static UserDaoImpl userDaoImpl;
	ConnDb connDb;
	ResultSet rs;
	public UserDaoImpl(){
		connDb=ConnDb.getInstance();
	}
	public static UserDaoImpl getInstance(){
		if(userDaoImpl==null){
			userDaoImpl=new UserDaoImpl();
		}
		return userDaoImpl;
	}
	@Override
	public Boolean isExistName(User user) {
		// TODO Auto-generated method stub
		rs=connDb.getSelect("select * from admin where username=?",user.getUsername());
		try {
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Boolean isRightPasswd(User user) {
		// TODO Auto-generated method stub
				rs=connDb.getSelect("select * from admin where username=? and password=?",user.getUsername(),user.getPassword());
				try {
					if(rs.next()){
						return true;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
	}
	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		connDb.insertAdmin(user.getUsername(), user.getPassword());
	}
}
