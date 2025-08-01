package com.nosliw.test.activity;

import java.io.FileNotFoundException;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTask1;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteTaskRhino;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.task.resource.HAPResourceDefinitionTaskSuite;
import com.nosliw.data.core.task.resource.HAPUtilityResourceTask;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;

public class HAPActivityMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "test";
//		String taskId = "sequenceTask";
		String taskId = "expression";
		String testData = "testData";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPResourceId resourceId = HAPUtilityResourceTask.buildResourceId(suite);
		HAPExecutableTaskSuite taskSuiteExe = runtimeEnvironment.getTaskManager().getTaskSuite(resourceId);
		
		HAPResourceDefinitionTaskSuite activitySuiteDefinition = (HAPResourceDefinitionTaskSuite)runtimeEnvironment.getResourceDefinitionManager().getLocalResourceDefinition(resourceId);
		
		Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(activitySuiteDefinition, testData);
		Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(taskSuiteExe.getValueStructureDefinitionWrapper().getValueStructureBlock(), input);

		HAPRuntimeTaskExecuteTaskRhino task = new HAPRuntimeTaskExecuteTaskRhino(new HAPInfoRuntimeTaskTask1(taskSuiteExe, taskId, inputById), runtimeEnvironment);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
