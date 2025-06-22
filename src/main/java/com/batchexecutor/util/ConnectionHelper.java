package com.batchexecutor.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionHelper {

	private static DataSource staticDataSource;

	private final DataSource injectedDataSource;

	public ConnectionHelper(DataSource dataSource) {
		this.injectedDataSource = dataSource;
	}

	public static Connection getConnection(boolean autoCommit) throws SQLException {
		Connection conn = staticDataSource.getConnection();
		conn.setAutoCommit(autoCommit);
		return conn;
	}

	@PostConstruct
	public void init() {
		staticDataSource = this.injectedDataSource;
	}
}
