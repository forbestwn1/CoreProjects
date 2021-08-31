package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.task.HAPProcessorTaskSuite;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;

public class HAPProcessorUIHandler {

	public static void process(HAPExecutableUIUnitPage pageUnit, HAPRuntimeEnvironment runtimeEnv) {
		processUnit(pageUnit, runtimeEnv);
	}
	
	private static void processUnit(HAPExecutableUIUnit uiUnit, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableTaskSuite taskSuite = createTaskSuite(uiUnit, runtimeEnv);
		uiUnit.getBody().setTaskSuite(taskSuite);
		
		for(HAPExecutableUIUnitTag tag: uiUnit.getBody().getUITags()) {
			processUnit(tag, runtimeEnv);
		}
	}
	
	private static HAPExecutableTaskSuite createTaskSuite(HAPExecutableUIUnit uiUnit, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionUIUnit uiUnitDef = uiUnit.getUIUnitDefinition();
		
		HAPContextProcessor contextProcess = new HAPContextProcessor(uiUnitDef, runtimeEnv);
		
		HAPExecutableTaskSuite out = HAPProcessorTaskSuite.process(
				null, 
				uiUnitDef.getHandlers(),
				contextProcess, 
				runtimeEnv,
				new HAPProcessTracker());
		return out;
	}
}
