package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionLibrary extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockDataExpressionLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBlockDataExpressionLibrary dataExpressionLibrary = (HAPManualBlockDataExpressionLibrary)entityDefinition;

		JSONArray dataExpressionArray = (JSONArray)jsonValue;
		for(int i=0; i<dataExpressionArray.length(); i++) {
			JSONObject elementObj = dataExpressionArray.getJSONObject(i);
			dataExpressionLibrary.addElement(HAPManualUtilityParserBrickFormatJson.parseWrapperValue(elementObj, HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, parseContext, getManualDivisionEntityManager(), getEntityManager()));
		}
	}

}
