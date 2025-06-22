package com.batchexecutor.service.exec;

import com.batchexecutor.enumeration.TriggerType;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class BatchExecutionService {
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;
	private final JobExplorer jobExplorer;

	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final ConcurrentMap<String, Future<?>> futureMap = new ConcurrentHashMap<>();

	@Autowired
	public BatchExecutionService(JobLauncher jobLauncher, JobRegistry jobRegistry, JobExplorer jobExplorer) {
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
		this.jobExplorer = jobExplorer;
	}

	/**
	 * ジョブを非同期実行（重複実行は禁止）
	 *
	 * @param jobName     実行したいjob名
	 * @param triggerType 実行元
	 * @return 実行状態
	 */
	public String run(String jobName, TriggerType triggerType) {
		return executeJob(jobName, createJobParameters(triggerType));
	}

	/**
	 * 最後に失敗したJobParametersで再実行
	 *
	 * @param jobName ジョブ名
	 * @return 実行結果メッセージ
	 */
	public String restart(String jobName) {

		JobExecution lastExecution = findLatestJobExecution(jobName)
				.orElseThrow(() -> new RuntimeException("ジョブ [" + jobName + "] の履歴が見つかりません"));

		if (lastExecution.getStatus() != BatchStatus.FAILED) {
			throw new RuntimeException("ジョブ [" + jobName + "] の最終実行が失敗ではないので再実行できません");
		}

		return executeJob(jobName, lastExecution.getJobParameters());
	}

	private String executeJob(String jobName, JobParameters parameters) {
		try {
			Job job = jobRegistry.getJob(jobName);

			// 重複実行チェック（DB上のJobExecutionが実行中か）
			if (isJobRunning(jobName)) {
				throw new RuntimeException("ジョブ [" + jobName + "] は現在実行中のため、起動できません");
			}

			// 非同期実行（FutureMapでトラッキング）
			Future<?> future = executor.submit(() -> {
				try {
					jobLauncher.run(job, parameters);
				} catch (Exception e) {
					throw new RuntimeException("ジョブ実行中にエラーが発生しました", e);
				} finally {
					futureMap.remove(jobName);
				}
			});

			futureMap.put(jobName, future);
			return "ジョブ [" + jobName + "] を開始しました";
		} catch (NoSuchJobException e) {
			throw new RuntimeException("ジョブ [" + jobName + "] は存在しません", e);
		} catch (Exception e) {
			throw new RuntimeException("ジョブ [" + jobName + "] の実行処理でエラーが発生しました。", e);
		}
	}

	/**
	 * 実行中ジョブを強制停止
	 *
	 * @param jobName JOBの名前
	 * @return 停止状況
	 */
	public String stop(String jobName) {
		try {
			jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			throw new RuntimeException("ジョブ [" + jobName + "] は存在しません", e);
		}

		Future<?> future = futureMap.get(jobName);
		if (future == null || !isJobRunning(jobName)) {
			throw new RuntimeException("ジョブ [" + jobName + "] が実行状態であることを確認できません");
		}

		if (future.isDone()) {
			throw new RuntimeException("ジョブ [" + jobName + "] はすでに終了しています");
		}

		//強制停止
		future.cancel(true);
		return "ジョブ [" + jobName + "] に強制停止を指示しました";
	}


	/**
	 * 実際に実行中なのかDBで確認をする
	 *
	 * @param jobName JOBの名前
	 * @return DB状で実行中になっているのか確認
	 */
	private boolean isJobRunning(String jobName) {
		Collection<JobExecution> executions = jobExplorer.findRunningJobExecutions(jobName);
		return !executions.isEmpty();
	}

	private Optional<JobExecution> findLatestJobExecution(String jobName) {
		List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 10);
		return instances.stream()
				.flatMap(instance -> jobExplorer.getJobExecutions(instance).stream())
				.max(Comparator.comparing(JobExecution::getCreateTime));
	}

	/**
	 * 実行パラメータを作成する
	 *
	 * @param triggerType 実行タイプ
	 * @return 構築済みJobParameters
	 */
	private JobParameters createJobParameters(TriggerType triggerType) {
		return new JobParametersBuilder()
				.addString("requestDateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))
				.addString("jobVersion", "v1.0.0")
				.addString("triggerType", triggerType.message())
				.toJobParameters();
	}
}
