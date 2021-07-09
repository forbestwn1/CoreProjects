package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

@HAPEntityWithAttribute
public class HAPExecutableTask extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	private String m_taskType;
	
	public HAPExecutableTask(String taskType) {
		this.m_taskType = taskType;
	}
	
	public String getTaskType() {   return this.m_taskType;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKTYPE, this.m_taskType);
	}
	
}
