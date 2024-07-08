package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionElementInLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualBlockDataExpressionElementInLibrary brick = (HAPManualBlockDataExpressionElementInLibrary)brickDefinition;
		HAPManualDataExpressionLibraryElement value = new HAPManualDataExpressionLibraryElement();
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		
		value.setExpression(this.getRuntimeEnvironment().getDataExpressionParser().parseExpression(value.getExpressionStr()));
		
		brick.setValue(value);
	}
}
