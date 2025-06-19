package com.batchexecutor.job;


import com.batchexecutor.service.TestService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.repository.JobRepository;

@Configuration
public class JobAConfig implements JobDefinition {

    @Autowired
    private TestService testService;

    @Override
    public String jobName() {
        return "jobA";
    }

    @Override
    public Step buildStep(JobRepository jobRepository) {
        return new StepBuilder("stepA", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobA: StepA 実行中");
                    testService.start();
                    return RepeatStatus.FINISHED;
                }, null)
                .build();
    }

    @Bean
    public Job jobA(JobRepository jobRepository) {
        return buildJob(jobRepository);
    }
}