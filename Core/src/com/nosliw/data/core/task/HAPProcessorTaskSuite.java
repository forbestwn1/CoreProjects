package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.attachment.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorTaskSuite {

	public static HAPExecutableTaskSuite process(
			String id,
			HAPDefinitionTaskSuite taskSuiteDef,
			HAPContextProcessor attachmentReferenceContext,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		
		HAPExecutableTaskSuite out = new HAPExecutableTaskSuite(id);
		out.setValueStructureDefinitionWrapper(taskSuiteDef.getValueStructureWrapper());
		
		for(HAPDefinitionTask taskDef : taskSuiteDef.getEntityElements()) {
			HAPInfoTask taskInfo = runtimeEnv.getTaskManager().getTaskInfo(taskDef.getTaskType());
			HAPExecutableTask taskExe = taskInfo.getProcessor().process(taskDef, id, attachmentReferenceContext, taskSuiteDef.getValueStructureWrapper(), processTracker);
			out.addEntityElement(taskExe);
		}
		
		return out;
	}
	
	
}
