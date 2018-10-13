package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.data.core.task.HAPExecutableTask;

public abstract class HAPRuntimeTaskExecuteTask extends HAPRuntimeTask{

	final public static String TASK = "ExecuteTask"; 
	
	private HAPExecutableTask m_task;
	
	private Map<String, Object> m_input;
	
	public HAPRuntimeTaskExecuteTask(HAPExecutableTask task, Map<String, Object> input) {
		this.m_task = task;
		this.m_input = input;
	}
	
	@Override
	public String getTaskType() {  return TASK;  	}

}
