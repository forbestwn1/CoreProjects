package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPElementInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionGroup extends HAPPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockDataExpressionGroup(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualBlockDataExpressionGroup.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualBrick brickDef, JSONObject jsonObj, HAPManualContextParse parseContext) {
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)brickDef;

		JSONArray dataExpressionArray = jsonObj.getJSONArray(HAPContainerDataExpression.ITEM);
		for(int i=0; i<dataExpressionArray.length(); i++) {
			JSONObject elementObj = dataExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				String expressionStr = elementObj.getString(HAPElementInContainerDataExpression.EXPRESSION);
				HAPDefinitionDataExpression dataExpression =  this.getRuntimeEnvironment().getDataExpressionParser().parseExpression(expressionStr);
				HAPManualDataExpressionItemInGroup item = new HAPManualDataExpressionItemInGroup(dataExpression);
				item.buildEntityInfoByJson(elementObj);
				groupBlock.getValue().addItem(item);
			}
		}
	}
}
