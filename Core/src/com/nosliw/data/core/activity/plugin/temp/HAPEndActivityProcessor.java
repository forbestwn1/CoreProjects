package com.nosliw.data.core.activity.plugin.temp;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.process1.HAPContextProcessor;
import com.nosliw.data.core.process1.HAPExecutableActivity;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionEmpty;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPEndActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe, HAPValueStructureDefinitionGroup processDataContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv, 
			HAPConfigureProcessorValueStructure configure, 
			HAPProcessTracker processTracker) {

		HAPEndActivityDefinition endActivity = (HAPEndActivityDefinition)activityDefinition;
		
		if(endActivity.getOutput()!=null) {
			//build result data association only when end activity has output
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(processDataContext), endActivity.getOutput(), HAPContainerStructure.createDefault(HAPValueStructureDefinitionEmpty.flatStructure()), null, runtimeEnv);
			results.put(endActivity.getName(), result);
		}
		else {  //kkkk
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(processDataContext), new HAPDefinitionDataAssociationMirror(), HAPContainerStructure.createDefault(processDataContext), null, runtimeEnv);
			results.put(endActivity.getName(), result);
		}
		
		HAPEndActivityExecutable out = new HAPEndActivityExecutable(id, activityDefinition);
		
		return out;
	}

}
