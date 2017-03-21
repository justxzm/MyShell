package com.myshell.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnDb {
	private String getDriver="com.mysql.jdbc.Driver";
	private String getConnection="jdbc:mysql://localhost:3306/myshell";
	private String getName="root";
	private String getPwd="jianchi";
	static Connection con;
	static PreparedStatement preStat;
	static ResultSet rs=null;
	static ConnDb connDb;
	public ConnDb(){
		try {
			Class.forName(getDriver);
			con=DriverManager.getConnection(getConnection,getName,getPwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	public Connection getConnection(){
		return con;
	}
	public static ConnDb getInstance(){
		if(connDb==null){
			connDb=new ConnDb();
		}
		return connDb;
	}
	public ResultSet getSelect(String sql,String... params){
		try {
			preStat=con.prepareStatement(sql);
			for(int i=0;i<params.length;i++){
				preStat.setObject(i+1, params[i]);
			}
			rs=preStat.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public void insertAdmin(String userName,String password){
		try {
			preStat=con.prepareStatement("insert into admin(username,password) values(?,?)");
			preStat.setString(1, userName );
			preStat.setString(2, password );
			preStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void closeAll(){
		try {
			if(rs!=null){
				rs.close();
			}
			if(preStat.isClosed()==false){
				preStat.close();
			}
			if(con.isClosed()==false){
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
