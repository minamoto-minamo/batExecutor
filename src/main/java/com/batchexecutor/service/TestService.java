package com.batchexecutor.service;


import com.batchexecutor.util.YamlConfigStore;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService extends AbstractService {

	@Override
	public void exec() throws Exception {
		try {
			Map<String, Object> customerConfig = YamlConfigStore.getInstance().getTableConfig("customer");
			String TableName = (String) customerConfig.get("table");
			System.out.println(TableName);
		} catch (Exception e) {
			throw e;
		}

	}
}