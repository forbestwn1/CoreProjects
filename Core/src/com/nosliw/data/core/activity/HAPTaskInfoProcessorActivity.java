package com.nosliw.data.core.activity;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPProcessorTask;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPTaskInfoProcessorActivity implements HAPProcessorTask{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPTaskInfoProcessorActivity(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public HAPExecutableTask process(
			HAPDefinitionTask taskDefinition, 
			String id,
			HAPContextProcessAttachmentReference processContext, 
			HAPWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker) {
		HAPDefinitionActivity activityDef = (HAPDefinitionActivity)taskDefinition;
		
		HAPExecutableActivity activityExe = m_runtimeEnv.getActivityManager().getPluginManager().getPlugin(activityDef.getType()).process(activityDef, activityDef.getId(), processContext, valueStructureWrapper, m_runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
		return activityExe;
	}

}
