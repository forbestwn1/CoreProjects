package com.nosliw.data.core.imp.cronjob;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.cronjob.HAPExecutableCronJob;

public class HAPInstanceCronJob extends HAPSerializableImp{

	private String m_id;
	
	private HAPExecutableCronJob m_cronJob;
	
	public HAPInstanceCronJob(String id, HAPExecutableCronJob cronJob) {
		this.m_id = id;
		this.m_cronJob = cronJob;
	}
	
	public String getId() {   return this.m_id;    }
	public void setId(String id) {   this.m_id = id;    }
	
	public HAPExecutableCronJob getCronJob() {   return this.m_cronJob;    }
	public void setCronJob(HAPExecutableCronJob job) {   this.m_cronJob = job;    }
	
}
