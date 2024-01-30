package com.classbook.cba.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
	private static Connection connection;
	
	public static Connection getConnection() {
		return connection == null ? createConnection() : connection;
	}
	
	private static Connection createConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://mysql-3b8e1b6b-personal-2024.a.aivencloud.com:25679/cb_app_db?sslmode=require",
					"avnadmin", "AVNS_rD4L1U0rBrjUFaLLs29");
			connection=conn;
			new Thread().wait(6000);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	


}
