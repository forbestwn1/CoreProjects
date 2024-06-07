package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.HAPUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualBrickTestComplexScript;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionLibrary extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, HAPManualBrickTestComplexScript.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBlockDataExpressionLibrary dataExpressionLibrary = (HAPManualBlockDataExpressionLibrary)entityDefinition;

		JSONArray dataExpressionArray = (JSONArray)jsonValue;
		for(int i=0; i<dataExpressionArray.length(); i++) {
			JSONObject elementObj = dataExpressionArray.getJSONObject(i);
			HAPManualAttribute valueStructureAttr = HAPUtilityParserBrickFormatJson.parseAttribute(null, elementObj, HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, null, parseContext, this.getManualDivisionEntityManager(), this.getEntityManager());
			dataExpressionLibrary.addElement(valueStructureAttr);
		}
	}

}
