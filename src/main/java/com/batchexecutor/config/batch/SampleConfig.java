package com.batchexecutor.config.batch;

import com.batchexecutor.config.base.AbstractJobConfig;
import com.batchexecutor.config.base.RetryableTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.batchexecutor.util.BatchNameUtil.createClassAndMethod;
import static com.batchexecutor.util.BatchNameUtil.inferJobName;

@Configuration
public class SampleConfig extends AbstractJobConfig {
	//クラス名のConfig以前部分を必ず指定すること。
	public static final String BATCH_NAME = "Sample";


	/**
	 * バッチグループの代替になる
	 */
	@Bean(name = BATCH_NAME)
	public Job job() {
		return buildSimpleJob(inferJobName(this.getClass()),
				step1(),
				step2()
		);
	}

	/**
	 * バッチ単体の代替
	 * リトライがある時の基本形
	 */
	@Bean(name = BATCH_NAME + "#step1")
	public Step step1() {
		return buildRetryableStep(createClassAndMethod(this.getClass()), new RetryableTasklet() {
			@Override
			protected RepeatStatus runWithRetry(StepContribution contribution, ChunkContext chunkContext) {

				//業務フロー
				//強制停止時にはInterruptedExceptionが発生する。
				// catchして終了処理を行うこと。

				//一定時間待機後再実行させるときにはNotReadyToExecuteExceptionを発生させる。
				//　実行待機をさせたいときは、上記ExceptionをThrowすること。

				return RepeatStatus.FINISHED;
			}
		});
	}

	/**
	 * バッチ単体の代替
	 * リトライなしならこれでも可
	 */
	@Bean(name = BATCH_NAME + "#step2")
	public Step step2() {
		return buildRetryableStep(createClassAndMethod(this.getClass()), new RetryableTasklet() {
			@Override
			protected RepeatStatus runWithRetry(StepContribution contribution, ChunkContext chunkContext) {

				//業務フロー
				//強制停止時にはInterruptedExceptionが発生する。
				// catchして終了処理を行うこと。


				return RepeatStatus.FINISHED;
			}
		});
	}
}
