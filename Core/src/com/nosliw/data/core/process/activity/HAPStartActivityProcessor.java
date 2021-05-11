package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPStartActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPValueStructureDefinitionGroup processDataContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker){
		
		HAPStartActivityExecutable out = new HAPStartActivityExecutable(id, activityDefinition);
		
		HAPStartActivityDefinition startActivity = (HAPStartActivityDefinition)activityDefinition;
		out.setFlow(startActivity.getFlow());
		
		return out;
	};

}
