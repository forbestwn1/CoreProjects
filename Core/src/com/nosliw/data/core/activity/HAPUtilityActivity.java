package com.nosliw.data.core.activity;

import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityActivity {

	//process result
	public static HAPExecutableResultActivity processNormalActivityResult(
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
			HAPValueStructure dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(dataAssociationInputContext), resultDef.getOutputDataAssociation(), HAPContainerStructure.createDefault(parentContext), null, null, runtimeEnv);
			resultExe.setDataAssociation(outputDataAssociation);
		}
		return resultExe;
	}

	public static void processNormalActivityInputDataAssocation(HAPExecutableActivity activity, HAPDefinitionActivityNormal activityDefinition, HAPValueStructure valueStructure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(valueStructure), 
				activityDefinition.getInputMapping(), 
				HAPContainerStructure.createDefault(activityDefinition.getInputContextStructure(valueStructure)), 
				null, 
				null,
				runtimeEnv);
		activity.setInputDataAssociation(da);
	}

}
