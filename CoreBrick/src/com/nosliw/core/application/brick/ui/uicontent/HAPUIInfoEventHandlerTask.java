package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPUIInfoEventHandlerTask extends HAPUIInfoEventHandler{

	@HAPAttribute
	public static final String TASKBRICKID = "taskBrickId";

	private HAPIdBrickInBundle m_taskBrickId;
	
	@Override
	public String getHandlerType() {   return HAPConstantShared.UICONTENT_EVENTHANDLERTYPE_TASK;   }

	public void setTaskBrickId(HAPIdBrickInBundle taskBrickId) {	this.m_taskBrickId = taskBrickId;	}
	
	public HAPIdBrickInBundle getTaskBrickId() {    return this.m_taskBrickId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKBRICKID, this.getTaskBrickId().toStringValue(HAPSerializationFormat.JSON));
	}
}
