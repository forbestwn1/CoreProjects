package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteProcess extends HAPRuntimeTask{

	final public static String TASK = "ExecuteProcess";
	
	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String INPUT = "input";

	private HAPExecutableProcess m_process;
	
	private Map<String, HAPData> m_input;
	
	public HAPRuntimeTaskExecuteProcess(HAPExecutableProcess process, Map<String, HAPData> input) {
		this.m_process = process;
		this.m_input = input;
	}
	
	public HAPExecutableProcess getProcess() {   return this.m_process;    }
	public Map<String, HAPData> getInput(){    return this.m_input;    }

	@Override
	public String getTaskType(){  return TASK; }
}
