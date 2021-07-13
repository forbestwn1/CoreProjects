package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
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
		HAPExecutableTaskSuite handlers = createHandler(uiUnit, runtimeEnv);
		uiUnit.getBody().setHandlers(handlers);
		
		for(HAPExecutableUIUnitTag tag: uiUnit.getBody().getUITags()) {
			processUnit(tag, runtimeEnv);
		}
	}
	
	private static HAPExecutableTaskSuite createHandler(HAPExecutableUIUnit uiUnit, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionUIUnit uiUnitDef = uiUnit.getUIUnitDefinition();
		
		HAPContextProcessAttachmentReference contextProcess = new HAPContextProcessAttachmentReference(uiUnitDef, runtimeEnv);
		
		HAPExecutableTaskSuite out = HAPProcessorTaskSuite.process(
				null, 
				uiUnitDef.getHandlers(),
				contextProcess, 
				null, 
				runtimeEnv,
				new HAPProcessTracker());
		return out;
	}
}
