package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionElementInLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualDefinitionBlockDataExpressionElementInLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockDataExpressionElementInLibrary brick = (HAPManualDefinitionBlockDataExpressionElementInLibrary)brickDefinition;
		HAPManualDataExpressionLibraryElement value = new HAPManualDataExpressionLibraryElement();

		HAPUtilityEntityInfo.buildEntityInfoByJson(jsonValue, brick);
		
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		
		value.setExpression(this.getRuntimeEnvironment().getDataExpressionParser().parseExpression(value.getExpressionStr()));
		
		brick.setValue(value);
	}
}
