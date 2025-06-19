package com.batchexecutor.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;

public interface JobDefinition {
    String jobName();
    Step buildStep(JobRepository jobRepository);

    default Job buildJob(JobRepository jobRepository) {
        return new JobBuilder(jobName(), jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(buildStep(jobRepository))
                .build();
    }
}
