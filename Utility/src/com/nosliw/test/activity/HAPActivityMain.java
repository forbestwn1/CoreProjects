package com.nosliw.test.activity;

import java.io.FileNotFoundException;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.activity.HAPExecutableActivitySuite;
import com.nosliw.data.core.activity.resource.HAPResourceDefinitionActivitySuite;
import com.nosliw.data.core.activity.resource.HAPUtilityResourceActivity;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteActivityRhino;
import com.nosliw.data.core.structure.data.HAPContextDataFlat;

public class HAPActivityMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "main";
		String activity = "expression";
		String testData = "testData";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableActivitySuite activitySuiteExe = runtimeEnvironment.getActivityManager().getActivitySuite(HAPUtilityResourceActivity.buildResourceId(suite));
		
		HAPContextDataFlat input = HAPUtilityAttachment.getTestDataFromAttachment((HAPResourceDefinitionActivitySuite)activitySuiteExe.getDefinition(), testData);
		
		HAPRuntimeTaskExecuteActivityRhino task = new HAPRuntimeTaskExecuteActivityRhino(activitySuiteExe, input, runtimeEnvironment.getResourceManager());
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
