package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPManagerTask {

	private Map<String, HAPInfoTask> m_taskInfo;
	
	public HAPManagerTask() {
		this.m_taskInfo = new LinkedHashMap<String, HAPInfoTask>();
	}
	
	public HAPInfoTask getTaskInfo(String taskType) {
		return this.m_taskInfo.get(taskType);
	}
	
}
