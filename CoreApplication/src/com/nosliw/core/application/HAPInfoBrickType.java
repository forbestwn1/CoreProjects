package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPInfoBrickType extends HAPSerializableImp{

	public static String TASKTYPE = "taskType";
	
	public static String ISTASK = "isTask";
	
	private String m_taskType;
	
	private boolean m_isTask = false;;
	
	public HAPInfoBrickType() {}
	
	public HAPInfoBrickType(String taskType) {
		this.m_taskType = taskType;
		this.m_isTask = true;
	}
	
	public HAPInfoBrickType(boolean isTask) {
		this.m_isTask = isTask;
	}
	
	public String getTaskType() {    return this.m_taskType;    }
	
	public boolean isTask() {    return this.m_isTask;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(TASKTYPE, this.m_taskType);
		jsonMap.put(ISTASK, this.m_isTask+"");
		typeJsonMap.put(ISTASK, Boolean.class);
	}
}
