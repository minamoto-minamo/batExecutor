package com.batchexecutor.service;


import org.springframework.stereotype.Service;

@Service
public class TestService extends AbstractService {

	@Override
	public void exec() throws Exception {
		try {
			System.out.println("test Job");
		} catch (Exception e) {
			throw e;
		}

	}
}