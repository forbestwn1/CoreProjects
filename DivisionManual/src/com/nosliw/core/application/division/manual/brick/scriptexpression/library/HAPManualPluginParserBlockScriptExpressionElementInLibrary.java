package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockScriptExpressionElementInLibrary extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockScriptExpressionElementInLibrary(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, HAPManualBlockScriptExpressionElementInLibrary.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualBlockScriptExpressionElementInLibrary brick = (HAPManualBlockScriptExpressionElementInLibrary)brickDefinition;
		HAPManualScriptExpressionLibraryElement value = new HAPManualScriptExpressionLibraryElement();
		value.buildObject(jsonValue, HAPSerializationFormat.JSON);
		brick.setValue(value);
	}
}
