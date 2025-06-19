package com.batchexecutor.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DynamicScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobA;

    @Autowired
    private Job jobB;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleJobA() throws Exception {
        jobLauncher.run(jobA, new JobParametersBuilder()
                .addDate("runDate", new Date())
                .toJobParameters());
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void scheduleJobB() throws Exception {
        jobLauncher.run(jobB, new JobParametersBuilder()
                .addDate("runDate", new Date())
                .toJobParameters());
    }
}