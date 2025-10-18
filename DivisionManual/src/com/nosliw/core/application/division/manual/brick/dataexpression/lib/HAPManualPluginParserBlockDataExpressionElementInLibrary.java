package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserBlockDataExpressionElementInLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserBlockDataExpressionElementInLibrary(
			HAPManualManagerBrick manualDivisionEntityMan, 
			HAPManagerApplicationBrick brickMan, 
			HAPParserDataExpression dataExpressionParser,
			HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualDefinitionBlockDataExpressionElementInLibrary.class, manualDivisionEntityMan, brickMan);
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionElementInLibrary brick = (HAPManualDefinitionBlockDataExpressionElementInLibrary)brickDefinition;

		HAPUtilityEntityInfo.buildEntityInfoByJson(jsonValue, brick);
		
		HAPDefinitionDataExpressionStandAlone value = HAPDefinitionDataExpressionStandAlone.parse((JSONObject)jsonValue, m_dataRuleMan);  
		value.setExpression(this.m_dataExpressionParser.parseExpression(value.getExpressionStr()));
		
		brick.setValue(value);
	}
}
