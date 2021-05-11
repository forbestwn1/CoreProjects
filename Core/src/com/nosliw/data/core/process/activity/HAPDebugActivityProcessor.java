package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPDebugActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPValueStructureDefinitionGroup processDataContext, 
			Map<String, HAPExecutableDataAssociation> processResults,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		 
		HAPDebugActivityDefinition debugActivityDef = (HAPDebugActivityDefinition)activityDefinition;
		HAPDebugActivityExecutable out = new HAPDebugActivityExecutable(id, debugActivityDef);
		
		//input
		HAPUtilityProcess.processNormalActivityInputDataAssocation(out, debugActivityDef, processDataContext, runtimeEnv);
		
		//process success result
		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, debugActivityDef, HAPConstantShared.ACTIVITY_RESULT_SUCCESS, null, null, runtimeEnv);
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}
}
