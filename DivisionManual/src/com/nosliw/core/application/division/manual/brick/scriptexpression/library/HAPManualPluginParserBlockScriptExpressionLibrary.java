package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockScriptExpressionLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockScriptExpressionLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockScriptExpressionLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualBlockScriptExpressionLibrary scriptExpressionLibrary = (HAPManualBlockScriptExpressionLibrary)entityDefinition;

		JSONArray scriptExpressionArray = (JSONArray)jsonValue;
		for(int i=0; i<scriptExpressionArray.length(); i++) {
			JSONObject elementObj = scriptExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				scriptExpressionLibrary.addElement((HAPManualBlockScriptExpressionElementInLibrary)HAPManualDefinitionUtilityParserBrickFormatJson.parseBrick(elementObj, HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
			}
		}
	}

}
