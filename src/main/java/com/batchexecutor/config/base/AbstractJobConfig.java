package com.batchexecutor.config.base;

import jakarta.annotation.Resource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;


public abstract class AbstractJobConfig implements JobConfig {

	@Resource
	protected JobRepository jobRepository;

	@Autowired
	protected PlatformTransactionManager transactionManager;

	protected JobBuilder jobBuilder(String jobName) {
		return new JobBuilder(jobName, jobRepository);
	}

	protected StepBuilder stepBuilder(String stepName) {
		return new StepBuilder(stepName, jobRepository);
	}

	protected Job buildSimpleJob(String jobName, Step... steps) {
		SimpleJobBuilder builder = jobBuilder(jobName).start(steps[0]);
		for (int i = 1; i < steps.length; i++) {
			builder = builder.next(steps[i]);
		}
		return builder.build();
	}

	protected Step buildTaskletStep(String stepName, Tasklet tasklet) {
		return stepBuilder(stepName)
				.tasklet(tasklet, transactionManager)
				.build();
	}

	protected Step buildRetryableStep(String stepName, RetryableTasklet tasklet) {
		return stepBuilder(stepName)
				.tasklet(tasklet.withRetry(), transactionManager)
				.build();
	}
}
