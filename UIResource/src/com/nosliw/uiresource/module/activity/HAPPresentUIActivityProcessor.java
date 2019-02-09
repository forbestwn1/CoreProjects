package com.nosliw.uiresource.module.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPPresentUIActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPExecutableProcess processExe, HAPContextGroup context,
			Map<String, HAPExecutableDataAssociationGroup> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement, 
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {
		HAPPresentUIActivityExecutable out = new HAPPresentUIActivityExecutable(id, (HAPPresentUIActivityDefinition)activityDefinition);
		return out;
	}

}
