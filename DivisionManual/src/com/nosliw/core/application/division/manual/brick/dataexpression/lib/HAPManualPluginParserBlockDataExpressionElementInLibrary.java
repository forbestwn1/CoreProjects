package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionElementInLibrary extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockDataExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBlockDataExpressionElementInLibrary brick = (HAPManualBlockDataExpressionElementInLibrary)brickDefinition;
		HAPManualDataExpressionLibraryElement value = new HAPManualDataExpressionLibraryElement();
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		brick.setValue(value);
	}
}