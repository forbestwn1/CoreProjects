package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;

public abstract class HAPDefinitionTaskImp extends HAPEntityInfoWritableImp implements HAPDefinitionTask{

	private String m_taskType;
	
	public HAPDefinitionTaskImp(String taskType) {
		this.m_taskType = taskType;
	}
	
	@Override
	public String getTaskType() {   return this.m_taskType;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKTYPE, this.getTaskType());
	}

	
}
