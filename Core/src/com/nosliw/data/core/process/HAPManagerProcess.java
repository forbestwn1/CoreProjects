package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;

public class HAPManagerProcess {
	
	private HAPManagerActivityPlugin m_pluginManager;
	
	private HAPEnvContextProcessor m_contextProcessEnv;
	
	public HAPManagerProcess(
			HAPManagerActivityPlugin pluginMan,
			HAPDataTypeHelper dataTypeHelper,
			HAPRuntime runtime,
			HAPExpressionSuiteManager expressionManager) {
		this.m_pluginManager = pluginMan;
		this.m_contextProcessEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionManager, null);
	}

	public HAPExecutableProcess getProcess(HAPIdProcess processId) {
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite(processId.getSuiteId(), this.getPluginManager());
		HAPExecutableProcess out = HAPProcessorProcess.process(processId.getProcessId(), suite, new HAPProcessContext(), this, this.m_contextProcessEnv);
		return out;
	}
	
	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
}
