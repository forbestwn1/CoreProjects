package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.structure.data.HAPContextData;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteProcessEmbeded extends HAPRuntimeTask{

	final public static String TASK = "ExecuteEmbededProcess";
	
	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String PARENTCONTEXT = "parentContext";

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;
	
	private HAPContextData m_parentContextData;
	
	public HAPRuntimeTaskExecuteProcessEmbeded(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData input) {
		this.m_process = process;
		this.m_parentContextData = input;
	}
	
	public HAPExecutableWrapperTask<HAPExecutableProcess> getProcess() {   return this.m_process;    }
	public HAPContextData getParentContextData(){    return this.m_parentContextData;    }
	@Override
	public String getTaskType(){  return TASK; }
}
