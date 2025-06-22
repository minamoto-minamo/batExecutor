function loadJobs() {
	fetch("/manager/jobs")
		.then(response => response.json())
		.then(data => {
			const tbody = document.getElementById("job-table-body");
			tbody.innerHTML = "";

			data.forEach(job => {
				const isDeleted = job.status === "DELETED";
				const isRunning = job.running;
				const isNeverExecuted = !job.startTime;

				const startDisabled = (isDeleted || isRunning);
				const stopDisabled = (isDeleted || isNeverExecuted || !isRunning);
				const restartDisabled = !job.restartable || job.status !== "FAILED" || job.running;

				const row = document.createElement("tr");
				row.innerHTML = `
		<td>${job.jobName}</td>
		<td>${job.status}</td>
		<td>${job.running && job.currentStep ? job.currentStep : "-"}</td>
		<td>${job.startTime || "-"}</td>
		<td>${job.endTime || "-"}</td>
		<td>
			<button class="btn btn-sm ${startDisabled ? "btn-secondary" : "btn-success"} btn-start"
				data-job-name="${job.jobName}" ${startDisabled ? "disabled" : ""}>
			<i class="bi bi-play-fill"></i>
			</button>
			<button class="btn btn-sm ${stopDisabled ? "btn-secondary" : "btn-danger"} btn-stop"
		    	data-job-name="${job.jobName}" ${stopDisabled ? "disabled" : ""}>
				<i class="bi bi-stop-fill"></i>
		  	</button>
		    <button class="btn btn-sm ${restartDisabled ? "btn-secondary" : "btn-warning"} btn-restart"
			    data-job-name="${job.jobName}" ${restartDisabled ? "disabled" : ""}>
			    <i class="bi bi-arrow-counterclockwise"></i>
		    </button>
		</td>
`;
				tbody.appendChild(row);
			});
		})
		.catch(err => console.error("エラー:", err));
}


function showConfirm(message) {
	return new Promise((resolve) => {
		document.getElementById("confirmModalMessage").textContent = message;

		const modal = new bootstrap.Modal(document.getElementById("confirmModal"));
		const okBtn = document.getElementById("confirmModalOkBtn");

		const onOk = () => {
			okBtn.removeEventListener("click", onOk);
			modal.hide();
			resolve(true);
		};

		okBtn.addEventListener("click", onOk);

		modal.show();
	});
}



document.addEventListener("DOMContentLoaded", () => {
	loadJobs();
	setInterval(loadJobs, 10000);
	document.getElementById("reload-btn").addEventListener("click", loadJobs);

	document.getElementById("job-table-body").addEventListener("click", async (e) => {
		const sleep = (time) => new Promise((resolve) => setTimeout(resolve, time));
		const btn = e.target.closest("button");
		if (!btn) return;

		const jobName = btn.getAttribute("data-job-name");

		// 開始ボタン
		if (btn.classList.contains("btn-start")) {
			if (await showConfirm(`${jobName}を実行しますか？`)) {
				await fetch(`/batch/${jobName}/start`, { method: "POST" });
				await sleep(500);
				loadJobs();
			}
		}

		// 終了ボタン
		if (btn.classList.contains("btn-stop")) {
			if (await showConfirm(`${jobName}を停止してもよろしいですか？`)) {
				await fetch(`/batch/${jobName}/stop`, { method: "POST" });
				await sleep(500);
				loadJobs();
			}
		}

		// 再実行ボタン
		if (btn.classList.contains("btn-restart")) {
			if (await showConfirm(`${jobName}を再実行しますか？`)) {
				await fetch(`/batch/${jobName}/restart`, { method: "POST" });
				await sleep(500);
				loadJobs();
			}
		}
	});
});

