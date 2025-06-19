package com.batchexecutor.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
public class JobBConfig {

    @Bean
    public Job jobB(JobRepository jobRepository, Step stepB) {
        return new JobBuilder("jobB", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepB)
                .build();
    }

    @Bean
    public Step stepB(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepB", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobB: StepB 実行中");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }
}