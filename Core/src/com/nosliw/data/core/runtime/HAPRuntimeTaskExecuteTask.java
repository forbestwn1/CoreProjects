package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.data.core.process1.HAPExecutableProcess;

public abstract class HAPRuntimeTaskExecuteTask extends HAPRuntimeTask{

	final public static String TASK = "ExecuteTask"; 
	
	private HAPExecutableProcess m_task;
	
	private Map<String, Object> m_input;
	
	public HAPRuntimeTaskExecuteTask(HAPExecutableProcess task, Map<String, Object> input) {
		this.m_task = task;
		this.m_input = input;
	}
	
	@Override
	public String getTaskType() {  return TASK;  	}

}
