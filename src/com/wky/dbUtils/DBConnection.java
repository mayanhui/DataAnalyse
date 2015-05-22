package com.wky.dbUtils;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	private static Connection conn=null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	/*
	 * ��̬������������ʱ�Զ�ִ�еģ����������ǲ��Ǿ�̬��������Ҫ���õģ������д�ھ�̬�������ⲿ��ô���ã�д��̬����ֱ��д�����о�������
                         �����Ҫ�ھ�̬�������д���룬����д������ֱ��д�������ˡ�
	* */
	static{
		Properties properties = new Properties();
		try {
			properties.load(DBConnection.class.getClassLoader().getResourceAsStream("./db.properties"));
			driver = properties.getProperty("driver");
			url = properties.getProperty("url");
			user = properties.getProperty("username");
			password = properties.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * �������ݿ�
	 * ����ɾ�̬�ķ���������Ҫnew�ֱ࣬��д��������
	 * */
	public static Connection getConnection(){
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn){
		if(conn != null) {				//���conn���Ӷ���Ϊ��
			try {
				conn.close();			//�ر�conn���Ӷ������
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(PreparedStatement conn){
		if(ps != null) {				//���psԤ�������Ϊ��
			try {
				ps.close();			//�ر�psԤ�������
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(ResultSet conn){
		if(rs != null) {				//���rs���������Ϊnull
			try {
				rs.close();				//�ر�rs���������
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
