package com.nosliw.data.core.runtime.js.rhino;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.resource.HAPProcessId;
import com.nosliw.data.core.process.resource.HAPResourceIdProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessEmbededRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public class HAPManagerProcessImp implements HAPManagerProcess{

	private HAPRuntime m_runtime;
	private HAPManagerProcessDefinition m_processDefMan;
	
	public HAPManagerProcessImp(HAPManagerProcessDefinition processDefMan, HAPRuntime runtime) {
		this.m_processDefMan = processDefMan;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPServiceData executeProcess(String processId, String suitId, Map<String, HAPData> input) {
		HAPExecutableProcess processExe = m_processDefMan.getProcess(new HAPResourceIdProcess(new HAPProcessId(suitId, processId)), null);
		return this.executeProcess(processExe, input);
	}

	@Override
	public HAPServiceData executeProcess(HAPExecutableProcess process, Map<String, HAPData> input) {
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(process, input);
		return this.m_runtime.executeTaskSync(task);
	}

	@Override
	public HAPServiceData executeProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process, Map<String, HAPData> input) {
		HAPRuntimeTaskExecuteProcessEmbededRhino task = new HAPRuntimeTaskExecuteProcessEmbededRhino(process, input);
		return this.m_runtime.executeTaskSync(task);
	}
	
//	@Override
//	public HAPServiceData executeProcess(String process, HAPDefinitionProcessSuite suite, Map<String, HAPData> input) {
//		HAPExecutableProcess processExe = m_processDefMan.getProcess(process, suite);
//		return this.executeProcess(processExe, input);
//	}
}
