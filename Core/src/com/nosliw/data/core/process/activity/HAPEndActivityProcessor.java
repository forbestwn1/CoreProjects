package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.structure.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.structure.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionEmpty;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPEndActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe, HAPStructureValueDefinitionGroup processDataContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv, 
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {

		HAPEndActivityDefinition endActivity = (HAPEndActivityDefinition)activityDefinition;
		
		if(endActivity.getOutput()!=null) {
			//build result data association only when end activity has output
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(processDataContext), endActivity.getOutput(), HAPParentContext.createDefault(HAPStructureValueDefinitionEmpty.flatStructure()), null, runtimeEnv);
			results.put(endActivity.getName(), result);
		}
		else {  //kkkk
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(processDataContext), new HAPDefinitionDataAssociationMirror(), HAPParentContext.createDefault(processDataContext), null, runtimeEnv);
			results.put(endActivity.getName(), result);
		}
		
		HAPEndActivityExecutable out = new HAPEndActivityExecutable(id, activityDefinition);
		
		return out;
	}

}
