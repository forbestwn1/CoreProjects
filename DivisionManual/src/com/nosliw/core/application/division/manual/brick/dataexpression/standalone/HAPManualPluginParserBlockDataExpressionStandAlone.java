package com.nosliw.core.application.division.manual.brick.dataexpression.standalone;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockDataExpressionStandAlone extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPParserDataExpression m_dataExpressionParser;
	
	public HAPManualPluginParserBlockDataExpressionStandAlone(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPParserDataExpression dataExpressionParser) {
		super(HAPEnumBrickType.DATAEXPRESSIONSTANDALONE_100, HAPManualDefinitionBlockDataExpressionStandAlone.class, manualDivisionEntityMan, brickMan);
		this.m_dataExpressionParser = dataExpressionParser;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionStandAlone brick = (HAPManualDefinitionBlockDataExpressionStandAlone)brickDefinition;
		HAPDefinitionDataExpressionStandAlone value = new HAPDefinitionDataExpressionStandAlone();

		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		
		value.setExpression(this.m_dataExpressionParser.parseExpression(value.getExpressionStr()));
		
		brick.setValue(value);
	}
}
