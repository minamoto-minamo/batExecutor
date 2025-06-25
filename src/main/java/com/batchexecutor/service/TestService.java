package com.batchexecutor.service;

import com.batchexecutor.service.base.AbstractService;
import com.batchexecutor.util.*;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@Service
public class TestService extends AbstractService {

	@Override
	public void exec() throws Exception {

		try {
			Map<String, Object> customerConfig = YamlConfigStore.getInstance().getTableConfig("customer");
			Map<String, Object> jobConfig = YamlConfigStore.getInstance().getJobConfig("customer");


			String nameCol = (String) YamlParser.resolveKey(customerConfig,"customer.column.name");

			String sql = SqlReader.readSql("customer/test");
			sql = StringResolver.of(sql)
					.replaceWith(customerConfig)
					.toString();

			try (Connection conn = ConnectionHelper.getConnection(true);
			     PreparedStatement pst = conn.prepareStatement(sql);
			) {
				pst.setString(1, "090%");
				try (ResultSet rs = pst.executeQuery();) {
					while (rs.next()) {
						System.out.println(rs.getString(nameCol));
					}
				}
			} catch (Exception e) {
				throw e;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}
}