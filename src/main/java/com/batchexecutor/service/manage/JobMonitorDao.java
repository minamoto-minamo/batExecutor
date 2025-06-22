package com.batchexecutor.service.manage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JobMonitorDao {



	public static void deleteLogs(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String sql = """
				DELETE FROM dbo.BATCH_STEP_EXECUTION_CONTEXT;
				DELETE FROM dbo.BATCH_STEP_EXECUTION;
				DELETE FROM dbo.BATCH_JOB_EXECUTION_CONTEXT;
				DELETE FROM dbo.BATCH_JOB_EXECUTION_PARAMS;
				DELETE FROM dbo.BATCH_JOB_EXECUTION;
				DELETE FROM dbo.BATCH_JOB_INSTANCE;
				""";
		stmt.executeUpdate(sql);
	}
}
