package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructureEmpty;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPEndActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, String id,
			HAPExecutableProcess processExe, HAPContextGroup processContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcessDefinition processManager,
			HAPRequirementContextProcessor contextProcessRequirement, 
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {

		HAPEndActivityDefinition endActivity = (HAPEndActivityDefinition)activityDefinition;
		
		if(endActivity.getOutput()!=null) {
			//build result data association only when end activity has output
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(processContext), endActivity.getOutput(), HAPParentContext.createDefault(HAPContextStructureEmpty.flatStructure()), null, contextProcessRequirement);
			results.put(endActivity.getName(), result);
		}
		else {  //kkkk
			HAPExecutableDataAssociation result = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(processContext), new HAPDefinitionDataAssociationMirror(), HAPParentContext.createDefault(processContext), null, contextProcessRequirement);
			results.put(endActivity.getName(), result);
		}
		
		HAPEndActivityExecutable out = new HAPEndActivityExecutable(id, activityDefinition);
		
		return out;
	}

}
