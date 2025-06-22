package com.batchexecutor.config.base;


import com.batchexecutor.exception.NotReadyToExecuteException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

public abstract class RetryableTasklet {

	public Tasklet withRetry() {
		return (contribution, chunkContext) -> {
			RetryTemplate template = new RetryTemplate();

			FixedBackOffPolicy backOff = new FixedBackOffPolicy();
			backOff.setBackOffPeriod(10_000); // 待機時間

			SimpleRetryPolicy policy = new SimpleRetryPolicy(6,
					Map.of(NotReadyToExecuteException.class, true));

			template.setRetryPolicy(policy);
			template.setBackOffPolicy(backOff);

			return template.execute(context -> runWithRetry(contribution, chunkContext));
		};
	}


	protected abstract RepeatStatus runWithRetry(StepContribution contribution, ChunkContext chunkContext) throws Exception;
}