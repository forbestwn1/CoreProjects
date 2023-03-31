package com.nosliw.data.core.activity;

import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityActivity {

	//process result
	public static HAPExecutableResultActivity processActivityResult(
			HAPExecutableActivity activity,
			HAPDefinitionActivityNormal activityDefinition,
			String resultName, 
			HAPValueStructure parentContext,
			HAPBuilderResultContext resultContextBuilder, 
			HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionResultActivity resultDef = activityDefinition.getResult(resultName);
		HAPExecutableResultActivity resultExe = new HAPExecutableResultActivity(resultDef);
		if(resultContextBuilder!=null) {
			//data association input context
			HAPValueStructure activityOutputValueStructure = resultContextBuilder.buildResultValueStructure(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(activityOutputValueStructure), resultDef.getOutputDataAssociation(), HAPContainerStructure.createDefault(parentContext), null, runtimeEnv);
			resultExe.setDataAssociation(outputDataAssociation);
		}
		return resultExe;
	}

	public static HAPExecutableDataAssociation processActivityInputDataAssocation(HAPDefinitionActivityNormal activityDefinition, HAPValueStructure externalValueStructure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(externalValueStructure), 
				activityDefinition.getInputDataAssociation(), 
				HAPContainerStructure.createDefault(activityDefinition.getInputValueStructureWrapper().getValueStructure()), 
				null,
				runtimeEnv);
		return da;
	}

}
