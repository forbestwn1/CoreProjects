package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroupExecutable;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;

public class HAPStartActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPExecutableProcess processExe,
			HAPContextGroup context,
			Map<String, HAPDefinitionDataAssociationGroupExecutable> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPEnvContextProcessor envContextProcessor,
			HAPProcessContext processContext){
		
		HAPStartActivityExecutable out = new HAPStartActivityExecutable(id, activityDefinition);
		
		HAPStartActivityDefinition startActivity = (HAPStartActivityDefinition)activityDefinition;
		out.setFlow(startActivity.getFlow());
		
		return out;
	};

}
