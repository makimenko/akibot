package com.akibot.engine2.logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.sun.org.apache.xml.internal.utils.ObjectPool;

public class ConnectionFactory {
	private static interface Singleton {
		final ConnectionFactory INSTANCE = new ConnectionFactory();
	}

	private final DataSource dataSource;

	private ConnectionFactory()  {
		System.out.println("Connecting.....");
		Properties properties = new Properties();
		properties.setProperty("user", "akibot");
		properties.setProperty("password", "akibot");
		
		System.out.println("Driver start.....");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Driver end.....");

		
		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>(null);
		System.out.println("A.....");
		
		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:oracle:thin:@//localhost:1521/XE", properties);
		System.out.println("B.....");
		
		new PoolableConnectionFactory(connectionFactory, null);
		System.out.println("C.....");

		this.dataSource = new PoolingDataSource(pool);
		System.out.println("done.....");
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}