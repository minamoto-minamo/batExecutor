package com.batchexecutor.service.manage;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public record JobStatusDto(
		String jobName,
		String status,
		String startTime,
		String endTime,
		String currentStep,
		boolean registered,
		boolean running,
		boolean restartable
) {
	public static JobStatusDto of(
			String jobName,
			String status,
			LocalDateTime start,
			LocalDateTime end,
			String currentStep,
			boolean registered,
			boolean running,
			boolean restartable
	) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		return JobStatusDto.builder()
				.jobName(jobName)
				.status(status)
				.startTime(start != null ? dtf.format(start) : null)
				.endTime(end != null ? dtf.format(end) : null)
				.currentStep(currentStep)
				.registered(registered)
				.running(running)
				.restartable(restartable)
				.build();
	}
}