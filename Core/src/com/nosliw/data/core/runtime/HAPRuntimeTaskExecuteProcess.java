package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.structure.data.HAPContextData;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteProcess extends HAPRuntimeTask{

	final public static String TASK = "ExecuteProcess";
	
	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String INPUT = "input";

	private HAPExecutableProcess m_process;
	
	private HAPContextData m_input;
	
	public HAPRuntimeTaskExecuteProcess(HAPExecutableProcess process, HAPContextData input) {
		this.m_process = process;
		this.m_input = input;
	}
	
	public HAPExecutableProcess getProcess() {   return this.m_process;    }
	public HAPContextData getInput(){    return this.m_input;    }

	@Override
	public String getTaskType(){  return TASK; }
}
