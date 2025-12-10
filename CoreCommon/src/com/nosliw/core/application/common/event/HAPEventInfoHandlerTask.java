package com.nosliw.core.application.common.event;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPEventInfoHandlerTask extends HAPEventInfoHandler{

	@HAPAttribute
	public static final String TASKBRICKID = "taskBrickId";

	private HAPIdBrickInBundle m_taskBrickId;
	
	@Override
	public String getHandlerType() {   return HAPConstantShared.EVENT_HANDLERTYPE_TASK;   }

	public void setTaskBrickId(HAPIdBrickInBundle taskBrickId) {	this.m_taskBrickId = taskBrickId;	}
	
	public HAPIdBrickInBundle getTaskBrickId() {    return this.m_taskBrickId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKBRICKID, this.getTaskBrickId().toStringValue(HAPSerializationFormat.JSON));
	}
}
