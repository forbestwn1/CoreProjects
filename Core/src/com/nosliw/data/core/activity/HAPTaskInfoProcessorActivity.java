package com.nosliw.data.core.activity;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPProcessorTask;

public class HAPTaskInfoProcessorActivity implements HAPProcessorTask{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPTaskInfoProcessorActivity(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public HAPExecutableTask process(
			HAPDefinitionTask taskDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPManualBrickWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker) {
		if(!HAPUtilityEntityInfo.isEnabled(taskDefinition))  return null;

		HAPDefinitionActivity activityDef = (HAPDefinitionActivity)taskDefinition;
		HAPExecutableActivity activityExe = m_runtimeEnv.getActivityManager().getPluginManager().getPlugin(activityDef.getActivityType()).process(activityDef, activityDef.getId(), processContext, valueStructureWrapper, m_runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
		return activityExe;
	}

}
