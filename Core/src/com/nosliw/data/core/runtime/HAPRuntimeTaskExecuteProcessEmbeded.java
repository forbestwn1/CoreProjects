package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteProcessEmbeded extends HAPRuntimeTask{

	final public static String TASK = "ExecuteEmbededProcess";
	
	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String INPUT = "input";

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;
	
	private Map<String, HAPData> m_input;
	
	public HAPRuntimeTaskExecuteProcessEmbeded(HAPExecutableWrapperTask<HAPExecutableProcess> process, Map<String, HAPData> input) {
		this.m_process = process;
		this.m_input = input;
	}
	
	public HAPExecutableWrapperTask<HAPExecutableProcess> getProcess() {   return this.m_process;    }
	public Map<String, HAPData> getInput(){    return this.m_input;    }
	@Override
	public String getTaskType(){  return TASK; }
}
