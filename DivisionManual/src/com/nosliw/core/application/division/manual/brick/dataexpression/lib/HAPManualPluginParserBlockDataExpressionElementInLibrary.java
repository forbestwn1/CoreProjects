package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockDataExpressionElementInLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPParserDataExpression m_dataExpressionParser;
	
	public HAPManualPluginParserBlockDataExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPParserDataExpression dataExpressionParser) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualDefinitionBlockDataExpressionElementInLibrary.class, manualDivisionEntityMan, brickMan);
		this.m_dataExpressionParser = dataExpressionParser;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionElementInLibrary brick = (HAPManualDefinitionBlockDataExpressionElementInLibrary)brickDefinition;
		HAPManualDataExpressionLibraryElement value = new HAPManualDataExpressionLibraryElement();

		HAPUtilityEntityInfo.buildEntityInfoByJson(jsonValue, brick);
		
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		
		value.setExpression(this.m_dataExpressionParser.parseExpression(value.getExpressionStr()));
		
		brick.setValue(value);
	}
}
