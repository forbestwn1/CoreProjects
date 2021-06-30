package com.nosliw.test.activity;

import java.io.FileNotFoundException;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.activity.HAPExecutableActivitySuite;
import com.nosliw.data.core.activity.resource.HAPResourceDefinitionActivitySuite;
import com.nosliw.data.core.activity.resource.HAPUtilityResourceActivity;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskActivity;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteActivityRhino;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;

public class HAPActivityMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "test";
		String activity = "expression";
		String testData = "testData";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableActivitySuite activitySuiteExe = runtimeEnvironment.getActivityManager().getActivitySuite(HAPUtilityResourceActivity.buildResourceId(suite));
		HAPResourceDefinitionActivitySuite activitySuiteDefinition = (HAPResourceDefinitionActivitySuite)activitySuiteExe.getDefinition();
		
		Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(activitySuiteDefinition, testData);
		Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(activitySuiteExe.getValueStructureDefinitionWrapper().getValueStructure(), input);

		HAPRuntimeTaskExecuteActivityRhino task = new HAPRuntimeTaskExecuteActivityRhino(new HAPInfoRuntimeTaskActivity(activitySuiteExe, activity, input), runtimeEnvironment);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
