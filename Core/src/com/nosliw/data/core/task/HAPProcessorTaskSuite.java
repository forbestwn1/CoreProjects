package com.nosliw.data.core.task;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorTaskSuite {

	public static HAPExecutableTaskSuite process(
			String id,
			HAPDefinitionTaskSuite taskSuiteDef,
			HAPContextProcessor processContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		
		HAPExecutableTaskSuite out = new HAPExecutableTaskSuite(id);
		out.setValueStructureDefinitionWrapper(taskSuiteDef.getValueStructureWrapper());
		
		for(HAPDefinitionTask taskDef : taskSuiteDef.getEntityElements()) {
			HAPInfoTask taskInfo = runtimeEnv.getTaskManager().getTaskInfo(taskDef.getTaskType());
			HAPExecutableTask taskExe = taskInfo.getProcessor().process(taskDef, id, processContext, taskSuiteDef.getValueStructureWrapper(), processTracker);
			out.addEntityElement(taskExe);
		}
		
		return out;
	}
}
