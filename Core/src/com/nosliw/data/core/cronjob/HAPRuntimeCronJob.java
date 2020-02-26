package com.nosliw.data.core.cronjob;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.HAPData;

public class HAPRuntimeCronJob {

	public HAPInstanceCronJob newJob(HAPExecutableCronJob cronJob, Map<String, HAPData> parms) {
		
	}

	public List<HAPInstanceCronJob> findValidCronJob(){
		
	}
	
	public void processCronJob(HAPInstanceCronJob cronJob) {
		executeTask(cronJob);
		
		finishTask();
	}
	
	public void finishTask() {
		
	}
	
	public void executeTask(HAPInstanceCronJob cronJob) {
		
	}
	
	public boolean ifStop(String id) {
		
	}
	
	
}
