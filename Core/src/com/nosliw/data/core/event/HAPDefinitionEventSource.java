package com.nosliw.data.core.event;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionEventSource extends HAPSerializableImp{

	@HAPAttribute
	public static String POLLTASK = "pollTask";

	@HAPAttribute
	public static String POLLSCHEDULE = "pollSchedule";

	//how to sechedule poll task
	private HAPDefinitionPollSchedule m_pollSchedule;
	
	//poll task that check if event happened
	private HAPDefinitionPollTask m_pollTask;
	
	public HAPDefinitionPollTask getPollTask() {
		return this.m_pollTask;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_pollTask = new HAPDefinitionPollTask();
		this.m_pollTask.buildObject(jsonObj.optJSONObject(POLLTASK), HAPSerializationFormat.JSON);
		
		this.m_pollSchedule = new HAPDefinitionPollScheduleImp();
		((HAPDefinitionPollScheduleImp)this.m_pollSchedule).buildObject(jsonObj.optJSONObject(POLLSCHEDULE), HAPSerializationFormat.JSON);
		
		return true;  
	}
}
