package cn.et.shop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtils {
	
	//newproperties��̬����
	static Properties p=new Properties();
	static{
		//��ȡjdbc.properties�ļ�
		InputStream is=DBUtils.class.getResourceAsStream("jdbc.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ����
	 */
	public static Connection getConnection() throws Exception{
		//��ȡurl
		String url=p.getProperty("url");
		//��ȡ����
		String driverClass=p.getProperty("driverClass");
		//��ȡ�û���
		String uname=p.getProperty("username");
		//��ȡ����
		String password=p.getProperty("password");
		Class.forName(driverClass);
		//��¼�ɹ�
		Connection conn=DriverManager.getConnection(url,uname,password);
		return conn;
	}
}
