package com.batchexecutor.controller;

import com.batchexecutor.enumeration.TriggerType;
import com.batchexecutor.service.exec.BatchExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

	private final BatchExecutionService executionService;

	@Autowired
	public BatchController(BatchExecutionService executionService) {
		this.executionService = executionService;
	}

	/**
	 * ジョブを非同期で起動
	 * POST /batch/{jobName}/start
	 */
	@PostMapping("/{jobName}/start")
	public ResponseEntity<String> startJob(@PathVariable String jobName) {
		try {
			String result = executionService.run(jobName, TriggerType.REST);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("ジョブ起動に失敗: " + e.getMessage());
		}
	}

	/**
	 * ジョブに強制停止を指示
	 * POST /batch/{jobName}/stop
	 */
	@PostMapping("/{jobName}/stop")
	public ResponseEntity<String> stopJob(@PathVariable String jobName) {
		String result = executionService.stop(jobName);
		return ResponseEntity.ok(result);
	}

	/**
	 * ジョブを最後に失敗した実行から再開
	 * POST /batch/{jobName}/restart
	 */
	@PostMapping("/{jobName}/restart")
	public ResponseEntity<String> restartJob(@PathVariable String jobName) {
		try {
			String result = executionService.restart(jobName);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("ジョブ再実行に失敗: " + e.getMessage());
		}
	}
}

