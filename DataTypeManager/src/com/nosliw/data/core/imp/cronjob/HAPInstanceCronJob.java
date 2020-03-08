package com.nosliw.data.core.imp.cronjob;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.cronjob.HAPExecutableCronJob;

public class HAPInstanceCronJob extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String CRONJOB = "cronJob";

	private String m_id;
	
	private HAPExecutableCronJob m_cronJob;
	
	public HAPInstanceCronJob() {}
	
	public HAPInstanceCronJob(String id, HAPExecutableCronJob cronJob) {
		this.m_id = id;
		this.m_cronJob = cronJob;
	}
	
	public String getId() {   return this.m_id;    }
	public void setId(String id) {   this.m_id = id;    }
	
	public HAPExecutableCronJob getCronJob() {   return this.m_cronJob;    }
	public void setCronJob(HAPExecutableCronJob job) {   this.m_cronJob = job;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.getId());
		jsonMap.put(CRONJOB, this.m_cronJob.toStringValue(HAPSerializationFormat.JSON));
	}
}
