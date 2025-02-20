package com.nosliw.core.application.division.manual.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskFlowActivityTask extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskFlowActivityTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, HAPManualDefinitionBlockTaskFlowActivityTask.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTaskFlowActivityTask activityBrick = (HAPManualDefinitionBlockTaskFlowActivityTask)entityDefinition;

		activityBrick.buildEntityInfoByJson(jsonObj);
		
		this.parseBrickAttributeJson(activityBrick, jsonObj, HAPBlockTaskFlowActivityTask.TASK, null, null, parseContext);

		JSONObject nextJsonObj = jsonObj.getJSONObject(HAPBlockTaskFlowActivity.NEXT);
		HAPTaskFlowNext next = new HAPTaskFlowNext();
		next.buildObject(nextJsonObj, HAPSerializationFormat.JSON);
		activityBrick.setNext(next);
		
	}
}
