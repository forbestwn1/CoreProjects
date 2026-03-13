package com.nosliw.core.application.common.event;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPPackageBrickInBundle;

public class HAPEventReferenceHandlerTask extends HAPEventReferenceHandler{

	@HAPAttribute
	public static final String TASKBRICKPACKAGE = "taskBrickPackage";

	private HAPPackageBrickInBundle m_taskBrickPackage;
	
	@Override
	public String getHandlerType() {   return HAPConstantShared.EVENT_HANDLERTYPE_TASK;   }

	public void setTaskBrickPackage(HAPPackageBrickInBundle taskBrickId) {	this.m_taskBrickPackage = taskBrickId;	}
	
	public HAPPackageBrickInBundle getTaskBrickPackage() {    return this.m_taskBrickPackage;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKBRICKPACKAGE, this.getTaskBrickPackage().toStringValue(HAPSerializationFormat.JSON));
	}
}
