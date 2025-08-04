package com.nosliw.core.application.division.manual.brick.wrapperbrick;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.wrapperbrick.HAPBrickWrapperBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickWrapperBrick extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickWrapperBrick(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.WRAPPERBRICK_100, HAPManualDefinitionBrickWrapperBrick.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrickWrapperBrick wrapperBrick = (HAPManualDefinitionBrickWrapperBrick)brickManual;
		
		JSONObject jsonObj = null;
		if(jsonValue instanceof JSONObject) {
			jsonObj = (JSONObject)jsonValue;
		}
		else {
			jsonObj = new JSONObject(jsonValue);
		}
		
		HAPUtilityEntityInfo.buildEntityInfoByJson(jsonObj.getJSONObject(HAPManualBrickWrapperBrick.INFO), wrapperBrick);
		
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(jsonObj.get(HAPManualDefinitionBrickWrapperBrick.BRICKTYPE));
		
		this.parseBrickAttributeJson(wrapperBrick, jsonObj, HAPBrickWrapperBrick.BRICK, brickTypeId, null, parseContext);
		
	}
}
