package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockScriptExpressionElementInLibrary extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockScriptExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, HAPManualBlockScriptExpressionElementInLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBlockScriptExpressionElementInLibrary brick = (HAPManualBlockScriptExpressionElementInLibrary)brickDefinition;
		HAPManualScriptExpressionLibraryElement value = new HAPManualScriptExpressionLibraryElement();
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		brick.setValue(value);
	}
}
