package com.nosliw.data.core.activity;

import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityActivity {

	//process result
	public static HAPExecutableResultActivity processActivityResult(
			HAPExecutableActivity activity,
			HAPDefinitionActivityNormal activityDefinition,
			String resultName, 
			HAPValueStructureInValuePort parentContext,
			HAPBuilderResultContext resultContextBuilder, 
			HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionResultActivity resultDef = activityDefinition.getResult(resultName);
		HAPExecutableResultActivity resultExe = new HAPExecutableResultActivity(resultDef);
		if(resultContextBuilder!=null) {
			//data association input context
			HAPValueStructureInValuePort activityOutputValueStructure = resultContextBuilder.buildResultValueStructure(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(activityOutputValueStructure), resultDef.getOutputDataAssociation(), HAPContainerStructure.createDefault(parentContext), null, runtimeEnv);
			resultExe.setDataAssociation(outputDataAssociation);
		}
		return resultExe;
	}

	public static HAPExecutableDataAssociation processActivityInputDataAssocation(HAPDefinitionActivityNormal activityDefinition, HAPValueStructureInValuePort externalValueStructure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(externalValueStructure), 
				activityDefinition.getInputDataAssociation(), 
				HAPContainerStructure.createDefault(activityDefinition.getInputValueStructureWrapper().getValueStructureBlock()), 
				null,
				runtimeEnv);
		return da;
	}

}
