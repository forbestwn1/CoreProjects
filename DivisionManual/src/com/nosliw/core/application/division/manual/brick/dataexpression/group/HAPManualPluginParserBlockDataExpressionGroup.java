package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionGroup extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockDataExpressionGroup(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualDefinitionBlockDataExpressionGroup.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDef, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionGroup groupBlock = (HAPManualDefinitionBlockDataExpressionGroup)brickDef;

		JSONArray dataExpressionArray = jsonObj.getJSONArray(HAPContainerDataExpression.ITEM);
		for(int i=0; i<dataExpressionArray.length(); i++) {
			JSONObject elementObj = dataExpressionArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
				String expressionStr = elementObj.getString(HAPItemInContainerDataExpression.DATAEXPRESSION);
				HAPDefinitionDataExpression dataExpression =  this.getRuntimeEnvironment().getDataExpressionParser().parseExpression(expressionStr);
				HAPManualDefinitionDataExpressionItemInContainer item = new HAPManualDefinitionDataExpressionItemInContainer(dataExpression);
				item.buildEntityInfoByJson(elementObj);
				groupBlock.getValue().addItem(item);
			}
		}
	}
}
