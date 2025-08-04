package com.nosliw.core.application.division.manual.brick.task.flow;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowFlow;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskFlowFlow extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskFlowFlow(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASK_TASK_FLOW_100, HAPManualDefinitionBlockTaskFlowFlow.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTaskFlowFlow flowBrick = (HAPManualDefinitionBlockTaskFlowFlow)entityDefinition;

		//task interactive
		this.parseTaskInterfaceAttribute(entityDefinition, jsonObj, parseContext);
		
		JSONObject startObj = (JSONObject)jsonObj.opt(HAPBlockTaskFlowFlow.START);
		HAPTaskFlowNext start = new HAPTaskFlowNext();
		start.buildObject(startObj, HAPSerializationFormat.JSON);
		flowBrick.setStart(start);
		
		JSONArray activityArray = (JSONArray)jsonObj.opt(HAPBlockTaskFlowFlow.ACTIVITY);
		for(int i=0; i<activityArray.length(); i++) {
			JSONObject activityObj = activityArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(activityObj)) {
				flowBrick.addActivity((HAPManualDefinitionBlockTaskFlowActivity)HAPManualDefinitionUtilityParserBrickFormatJson.parseBrick(activityObj, HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
			}
		}
	}
}
