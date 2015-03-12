package com.akibot.engine2.logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class AkiConnectionFactory {

	private static interface Singleton {
		final AkiConnectionFactory INSTANCE = new AkiConnectionFactory();
	}

	private final DataSource dataSource;

	private AkiConnectionFactory() {

		Properties properties = new Properties();
		properties.setProperty("user", "akibot");
		properties.setProperty("password", "akibot");
		Locale.setDefault(Locale.ENGLISH);
		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>(null);
		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:oracle:thin:@192.168.0.106:1521:XE", properties);
		new PoolableConnectionFactory(connectionFactory, pool, null, "select 1 from dual", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);
		this.dataSource = new PoolingDataSource(pool);
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}

}