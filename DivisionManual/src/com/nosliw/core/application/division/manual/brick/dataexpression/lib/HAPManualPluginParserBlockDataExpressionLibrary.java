package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualDefinitionBlockDataExpressionLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionLibrary dataExpressionLibrary = (HAPManualDefinitionBlockDataExpressionLibrary)entityDefinition;

		JSONArray dataExpressionArray = (JSONArray)jsonValue;
		for(int i=0; i<dataExpressionArray.length(); i++) {
			JSONObject elementObj = dataExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				dataExpressionLibrary.addElement(HAPManualDefinitionUtilityParserBrickFormatJson.parseWrapperValue(elementObj, HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
			}
		}
	}

}
