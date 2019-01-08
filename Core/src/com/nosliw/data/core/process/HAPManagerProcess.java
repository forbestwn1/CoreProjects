package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPManagerProcess {
	
	private HAPManagerActivityPlugin m_pluginManager;
	
	private HAPRequirementContextProcessor m_contextProcessRequirement;
	
	public HAPManagerProcess(
			HAPManagerActivityPlugin pluginMan,
			HAPDataTypeHelper dataTypeHelper,
			HAPRuntime runtime,
			HAPExpressionSuiteManager expressionManager) {
		this.m_pluginManager = pluginMan;
		this.m_contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionManager, null);
	}

	public HAPExecutableProcess getProcess(HAPIdProcess processId) {
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite(processId.getSuiteId(), this.getPluginManager());
		HAPExecutableProcess out = HAPProcessorProcess.process(processId.getProcessId(), suite, this, this.m_contextProcessRequirement, new HAPProcessTracker());
		return out;
	}
	
	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
}
