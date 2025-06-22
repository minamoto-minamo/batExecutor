package com.batchexecutor.init;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class JobExecutionRecovery implements ApplicationListener<ApplicationReadyEvent> {

	private final JobExplorer jobExplorer;
	private final JobRepository jobRepository;

	public JobExecutionRecovery(JobExplorer jobExplorer, JobRepository jobRepository) {
		this.jobExplorer = jobExplorer;
		this.jobRepository = jobRepository;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		for (String jobName : jobExplorer.getJobNames()) {
			Collection<JobExecution> executions = jobExplorer.findRunningJobExecutions(jobName);
			for (JobExecution execution : executions) {
				execution.setStatus(BatchStatus.FAILED);
				execution.setEndTime(LocalDateTime.now());
				jobRepository.update(execution);
				System.out.println("未終了ジョブ [" + jobName + "] (ID: " + execution.getId() + ") をFAILEDにしました");
			}
		}
	}
}

