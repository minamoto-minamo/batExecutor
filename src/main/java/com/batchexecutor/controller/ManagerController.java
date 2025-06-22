package com.batchexecutor.controller;

import com.batchexecutor.service.manage.JobMonitorService;
import com.batchexecutor.service.manage.JobStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	private final JobMonitorService monitorService;

	@Autowired
	public ManagerController(JobMonitorService monitorService) {
		this.monitorService = monitorService;
	}

	@GetMapping("/home")
	public String showBatchPage(Model model) {
		model.addAttribute("title", "バッチ管理画面");
		model.addAttribute("jsVersion", System.currentTimeMillis());
		return "batch"; // → templates/batch.html
	}

	@GetMapping("/jobs")
	public ResponseEntity<List<JobStatusDto>> jobList() {
		List<JobStatusDto> jobs = monitorService.findAllJobs();
		if (jobs.isEmpty()) {
			return ResponseEntity.noContent().build(); // 204
		}
		return ResponseEntity.ok(jobs);
	}

	@GetMapping("/logDelete")
	public ResponseEntity<Boolean> deleteJobExecuteLog() {
		boolean isDeleted = monitorService.deleteExecLogs();
		return ResponseEntity.ok(isDeleted);
	}


}