package com.nosliw.core.application.division.manual.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskFlowActivity extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskFlowActivity(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass, HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTaskFlowActivity activityBrick = (HAPManualDefinitionBlockTaskFlowActivity)entityDefinition;

		activityBrick.buildEntityInfoByJson(jsonObj);

		JSONObject nextJsonObj = jsonObj.getJSONObject(HAPBlockTaskFlowActivity.NEXT);
		HAPTaskFlowNext next = new HAPTaskFlowNext();
		next.buildObject(nextJsonObj, HAPSerializationFormat.JSON);
		activityBrick.setNext(next);
		
	}
}
