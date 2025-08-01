package com.nosliw.core.application.division.manual.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskFlowActivityTask extends HAPManualPluginParserBlockTaskFlowActivity{

	public HAPManualPluginParserBlockTaskFlowActivityTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, HAPManualDefinitionBlockTaskFlowActivityTask.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		super.parseComplexDefinitionContentJson(entityDefinition, jsonObj, parseContext);
		
		HAPManualDefinitionBlockTaskFlowActivityTask activityBrick = (HAPManualDefinitionBlockTaskFlowActivityTask)entityDefinition;
		this.parseBrickAttributeJson(activityBrick, jsonObj, HAPBlockTaskFlowActivityTask.TASK, null, null, parseContext);

	}
}
