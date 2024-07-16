package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockScriptExpressionGroup extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockScriptExpressionGroup(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100, HAPManualDefinitionBlockScriptExpressionGroup.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDef, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockScriptExpressionGroup groupBlock = (HAPManualDefinitionBlockScriptExpressionGroup)brickDef;

		JSONArray scriptExpressionArray = jsonObj.getJSONArray(HAPContainerScriptExpression.ITEM);
		for(int i=0; i<scriptExpressionArray.length(); i++) {
			JSONObject elementObj = scriptExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				String expressionStr = elementObj.getString(HAPItemInContainerScriptExpression.SCRIPTEXPRESSION);
				HAPManualDefinitionScriptExpressionItemInContainer item = new HAPManualDefinitionScriptExpressionItemInContainer(expressionStr);
				groupBlock.getValue().addItem(item);
			}
		}
	}

}
