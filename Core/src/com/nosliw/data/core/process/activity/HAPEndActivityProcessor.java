package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPProcessorDataAssociation;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPEndActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, String id,
			HAPExecutableProcess processExe, HAPContextGroup context,
			Map<String, HAPExecutableDataAssociationGroup> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement, HAPProcessTracker processTracker) {

		HAPEndActivityDefinition endActivity = (HAPEndActivityDefinition)activityDefinition;
		
		HAPExecutableDataAssociationGroup result = HAPProcessorDataAssociation.processDataAssociation(context, endActivity.getOutput(), contextProcessRequirement);
		results.put(endActivity.getName(), result);
		
		HAPEndActivityExecutable out = new HAPEndActivityExecutable(id, activityDefinition);
		
		return out;
	}

}
