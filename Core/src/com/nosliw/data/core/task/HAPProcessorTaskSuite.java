package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPContextProcessAttachmentReferenceActivity;
import com.nosliw.data.core.activity.HAPExecutableActivitySuite;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorTaskSuite {

	public static HAPExecutableTaskSuite process(
			String id,
			HAPDefinitionTaskSuite taskSuiteDef,
			HAPContextProcessAttachmentReferenceActivity attachmentReferenceContext,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		
		HAPExecutableActivitySuite out = new HAPExecutableActivitySuite(taskSuiteDef);
		
		for(HAPDefinitionTask taskDef : taskSuiteDef.getEntityElements()) {
			HAPExecutableActivity activityExe = runtimeEnv.getActivityManager().getPluginManager().getPlugin(taskDef.getType()).process(taskDef, taskDef.getId(), attachmentReferenceContext, taskSuiteDef.getValueStructureWrapper(), runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
			
		}
		
		return out;
	}
	
	
}
