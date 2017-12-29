package cn.et.shop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtils {
	
	//newproperties静态方法
	static Properties p=new Properties();
	static{
		//获取jdbc.properties文件
		InputStream is=DBUtils.class.getResourceAsStream("jdbc.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取连接
	 */
	public static Connection getConnection() throws Exception{
		//获取url
		String url=p.getProperty("url");
		//获取驱动
		String driverClass=p.getProperty("driverClass");
		//获取用户名
		String uname=p.getProperty("username");
		//获取密码
		String password=p.getProperty("password");
		Class.forName(driverClass);
		//登录成功
		Connection conn=DriverManager.getConnection(url,uname,password);
		return conn;
	}
}
