package com.nosliw.data.core.imp.cronjob;

import com.nosliw.data.core.cronjob.HAPExecutableCronJob;

public class HAPInstanceCronJob {

	private String m_id;
	
	private HAPExecutableCronJob m_cronJob;
	
	public HAPInstanceCronJob(String id, HAPExecutableCronJob cronJob) {
		this.m_id = id;
		this.m_cronJob = cronJob;
	}
	
	public String getId() {   return this.m_id;    }
	public HAPExecutableCronJob getCronJob() {   return this.m_cronJob;    }
	
}
