package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionScriptExpressionItemInContainer;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
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
				HAPDefinitionScriptExpression scriptExpression = new HAPDefinitionScriptExpression();
				scriptExpression.buildObject(elementObj.get(HAPItemInContainerScriptExpression.SCRIPTEXPRESSION), HAPSerializationFormat.JSON);
				HAPDefinitionScriptExpressionItemInContainer item = new HAPDefinitionScriptExpressionItemInContainer(scriptExpression);
				item.buildEntityInfoByJson(elementObj);
				groupBlock.getValue().addItem(item);
			}
		}
	}

}
