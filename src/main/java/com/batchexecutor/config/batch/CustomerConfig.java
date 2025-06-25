package com.batchexecutor.config.batch;

import com.batchexecutor.config.base.AbstractJobConfig;
import com.batchexecutor.config.base.RetryableTasklet;
import com.batchexecutor.enumeration.BatchResult;
import com.batchexecutor.exception.NotReadyToExecuteException;
import com.batchexecutor.logging.Loggable;
import com.batchexecutor.service.TestService;
import com.batchexecutor.util.ConnectionHelper;
import com.batchexecutor.util.YamlConfigStore;
import com.batchexecutor.util.YamlParser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Map;

import static com.batchexecutor.util.BatchNameUtil.createClassAndMethod;
import static com.batchexecutor.util.BatchNameUtil.inferJobName;

@Configuration
public class CustomerConfig extends AbstractJobConfig implements Loggable {
	//クラス名のConfig以前部分を必ず指定すること。
	public static final String BATCH_NAME = "Customer";


	private final TestService testService;
	@Autowired
	public CustomerConfig(TestService testService) {
		this.testService = testService;
	}


	@Bean(name = BATCH_NAME)
	public Job job() {
		return buildSimpleJob(inferJobName(this.getClass()),
				bulkInsert(),
				summary()
		);
	}

	//実行待機があるときのStep
	@Bean(name = BATCH_NAME + "#bulkInsert")
	public Step bulkInsert() {
		return buildRetryableStep(createClassAndMethod(this.getClass()), new RetryableTasklet() {
			@Override
			protected RepeatStatus runWithRetry(StepContribution contribution, ChunkContext chunkContext) throws InterruptedException {
				try (Connection conn = ConnectionHelper.getConnection(false)) {
					for (int i = 0; i < 5; i++) {
						System.out.println("処理中..." + i);
						Thread.sleep(1000);
					}


					Map<String, Object> batConfig = YamlConfigStore.getInstance().getJobConfig("customer");
					String csvFilePath = (String) YamlParser.resolveKey(batConfig,"path.file.csv");

					if (!Files.exists(Path.of(csvFilePath))) {
						throw new NotReadyToExecuteException();
					}

					//bulkInsertToTable(TableName,csvFilePath);


				} catch (InterruptedException e) {
					logError(e);
					throw e;
				} catch (NotReadyToExecuteException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return RepeatStatus.FINISHED;
			}
		});
	}

	@Bean(name = BATCH_NAME + "#summary")
	public Step summary() {
		return buildRetryableStep(createClassAndMethod(this.getClass()), new RetryableTasklet() {
			@Override
			protected RepeatStatus runWithRetry(StepContribution contribution, ChunkContext chunkContext) {
				BatchResult res = testService.start();
				System.out.println(res.message());

				return RepeatStatus.FINISHED;
			}
		});
	}
}
