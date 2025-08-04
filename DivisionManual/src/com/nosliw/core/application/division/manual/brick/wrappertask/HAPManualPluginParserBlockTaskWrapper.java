package com.nosliw.core.application.division.manual.brick.wrappertask;

import org.json.JSONObject;

import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskWrapper extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockTaskWrapper(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualDefinitionBlockTaskWrapper.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
	
		HAPManualDefinitionBlockTaskWrapper taskWrapperDef = (HAPManualDefinitionBlockTaskWrapper)entityDefinition;
		taskWrapperDef.setTaskBrickType(HAPUtilityBrickId.parseBrickTypeId(jsonObj.get(HAPManualDefinitionBlockTaskWrapper.TASKBRICKTYPE)));
		
		this.parseBrickAttributeJson(entityDefinition, jsonObj, HAPBlockTaskWrapper.TASK, taskWrapperDef.getTaskBrickType(), null, parseContext);
	}
}
