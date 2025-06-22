package com.batchexecutor.config;

import com.batchexecutor.config.batch.SampleConfig;
import com.batchexecutor.enumeration.TriggerType;
import com.batchexecutor.service.exec.BatchExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.batchexecutor.util.BatchNameUtil.inferJobName;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	private final BatchExecutionService batchExecutionService;

	@Autowired
	public SchedulerConfig(BatchExecutionService batchExecutionService) {
		this.batchExecutionService = batchExecutionService;
	}

	@Scheduled(cron = "0 0 * * * *")
	public void scheduledJob() throws Exception {
		batchExecutionService.run(inferJobName(SampleConfig.class), TriggerType.SCHEDULER);
	}
}
