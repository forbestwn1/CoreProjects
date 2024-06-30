package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockScriptExpressionLibrary extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockScriptExpressionLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockScriptExpressionLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBlockScriptExpressionLibrary scriptExpressionLibrary = (HAPManualBlockScriptExpressionLibrary)entityDefinition;

		JSONArray scriptExpressionArray = (JSONArray)jsonValue;
		for(int i=0; i<scriptExpressionArray.length(); i++) {
			JSONObject elementObj = scriptExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				scriptExpressionLibrary.addElement(HAPManualUtilityParserBrickFormatJson.parseWrapperValue(elementObj, HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
			}
		}
	}

}
