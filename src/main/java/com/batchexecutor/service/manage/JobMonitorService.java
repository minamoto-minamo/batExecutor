package com.batchexecutor.service.manage;

import com.batchexecutor.util.ConnectionHelper;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JobMonitorService {

	private final JobExplorer jobExplorer;
	private final JobRegistry jobRegistry;

	public JobMonitorService(JobExplorer jobExplorer, JobRegistry jobRegistry) {
		this.jobExplorer = jobExplorer;
		this.jobRegistry = jobRegistry;
	}

	public List<JobStatusDto> findAllJobs() {
		Set<String> jobNames = new HashSet<>();
		jobNames.addAll(jobExplorer.getJobNames());
		jobNames.addAll(jobRegistry.getJobNames());

		List<JobStatusDto> results = new ArrayList<>();

		for (String jobName : jobNames) {

			boolean registered = jobRegistry.getJobNames().contains(jobName);

			if (!registered) {
				results.add(new JobStatusDto(jobName, "DELETED", null, null, null,false, false, false));
				continue;
			}

			List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 1);
			if (instances.isEmpty()) {
				results.add(new JobStatusDto(jobName, "NEVER", null, null, null,true, false, true));
				continue;
			}

			JobInstance instance = instances.get(0);
			JobExecution latest = jobExplorer.getJobExecutions(instance).get(0);

			boolean isRunning = latest.isRunning();
			boolean isRestartable = false;
			String runningStepName = null;

			try {
				isRestartable = jobRegistry.getJob(jobName).isRestartable();
			} catch (Exception e) {
				// ログ出力など必要に応じて
			}

			// 実行中のStepを取得（あれば）
			for (StepExecution stepExec : latest.getStepExecutions()) {
				if (stepExec.getStatus() == BatchStatus.STARTED) {
					runningStepName = stepExec.getStepName();
					break;
				}
			}

			results.add(JobStatusDto.of(
					jobName,
					latest.getStatus().toString(),
					latest.getStartTime(),
					latest.getEndTime(),
					runningStepName,
					true,
					isRunning,
					isRestartable
			));
		}

		return results;
	}

	public boolean deleteExecLogs() {
		try (Connection conn = ConnectionHelper.getConnection(true)){
			JobMonitorDao.deleteLogs(conn);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

