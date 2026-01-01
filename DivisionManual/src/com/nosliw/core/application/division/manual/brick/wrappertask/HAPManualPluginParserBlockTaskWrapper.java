package com.nosliw.core.application.division.manual.brick.wrappertask;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockTaskWrapper extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockTaskWrapper(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualDefinitionBlockTaskWrapper.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
	
		HAPManualDefinitionBlockTaskWrapper taskWrapperDef = (HAPManualDefinitionBlockTaskWrapper)entityDefinition;
		taskWrapperDef.setTaskBrickType(HAPUtilityBrickId.parseBrickTypeId(jsonObj.get(HAPManualDefinitionBlockTaskWrapper.TASKBRICKTYPE)));
		
		this.parseBrickAttributeJson(entityDefinition, jsonObj, HAPBlockTaskWrapper.TASK, taskWrapperDef.getTaskBrickType(), null, parseContext);
	}
}
