package com.nosliw.data.core.imp.cronjob;

import java.util.List;
import java.util.Map;

import com.nosliw.core.data.HAPData;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.common.structure.data.HAPContextDataGroup;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.cronjob.HAPExecutableCronJob;
import com.nosliw.data.core.cronjob.HAPInstancePollSchedule;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;

public class HAPRuntimeCronJob {

	private HAPManagerCronJob m_cronJobMan = null;

	private HAPCronJobInstanceSerializer m_cronJobInstanceSerializer;

	public HAPDataAccess m_dataAccess = null;
	
	public HAPRuntimeCronJob(HAPManagerCronJob cronJobMan, HAPManagerActivityPlugin activityPluginMan) {
		this.m_cronJobMan = cronJobMan;
		this.m_cronJobInstanceSerializer = new HAPCronJobInstanceSerializer(activityPluginMan); 
		this.m_dataAccess = new HAPDataAccess(this.m_cronJobInstanceSerializer);
	}
	
	public HAPInstanceCronJob newJob(HAPResourceId cronJobId, Map<String, HAPData> parms) {
		HAPExecutableCronJob cronJob = this.m_cronJobMan.getCronJob(cronJobId);
		return this.newJob(cronJob, parms);
	}
	
	public HAPInstanceCronJob newJob(HAPExecutableCronJob cronJob, Map<String, HAPData> parms) {
		HAPInstanceCronJob instance = new HAPInstanceCronJob(null, cronJob);
		HAPInstanceCronJob out = this.m_dataAccess.updateOrNewCronJob(instance);
		
		HAPInstancePollSchedule pollSchedule = cronJob.getSchedule().newPoll();
		HAPCronJobState state = new HAPCronJobState(null, out.getId(), pollSchedule, parms);
		this.m_dataAccess.updateOrNewState(state);
		return out;
	}

	public void execute() {
		List<HAPCronJobState> jobStates = this.m_dataAccess.findAllValidJobState();
		for(HAPCronJobState jobState : jobStates) {
			this.processCronJob(jobState);
		}
	}
	
	public void processCronJob(HAPCronJobState jobState) {
		
		HAPInstanceCronJob cronJob = this.m_dataAccess.getCronJob(jobState.getCronJobId());
		
		//task
		HAPContextDataGroup state = executeTask(cronJob, jobState.getState());
		
		//next schedule
		HAPInstancePollSchedule newSchedule = cronJob.getCronJob().getSchedule().prepareForNextPoll(jobState.getSchedule());
		jobState.setSchedule(newSchedule);
		
		this.m_dataAccess.updateOrNewState(jobState);
		
		//if finish
		finishTask(state);
	}
	
	public void finishTask(HAPContextDataGroup state) {
		
	}
	
	public HAPContextDataGroup executeTask(HAPInstanceCronJob cronJob, Map<String, HAPData> state) {
		return null;
	}
	
	public boolean ifStop(String id) {
		return true;
	}
	
}
