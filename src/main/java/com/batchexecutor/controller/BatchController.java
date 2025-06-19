package com.batchexecutor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job jobA;
    private final Job jobB;

    @PostMapping("/run/{jobName}")
    public String run(@PathVariable String jobName) throws Exception {
        Job job;
        switch (jobName) {
            case "jobA":
                job = jobA;
                break;
            case "jobB":
                job = jobB;
                break;
            default:
                return "不明なジョブ名: " + jobName;
        }
        jobLauncher.run(job, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
        return jobName + " 実行開始";
    }
}