package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPInfoBrickType extends HAPSerializableImp{

	public static String TASKTYPE = "taskType";
	
	private String m_taskType;
	
	public HAPInfoBrickType() {}
	
	public HAPInfoBrickType(String taskType) {
		this.m_taskType = taskType;
	}
	
	public String getTaskType() {    return this.m_taskType;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(TASKTYPE, this.m_taskType);
	}
}
