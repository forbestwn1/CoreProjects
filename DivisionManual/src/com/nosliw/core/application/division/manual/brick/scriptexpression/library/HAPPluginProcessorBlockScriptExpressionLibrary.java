package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPPluginProcessorBlockScriptExpressionLibrary extends HAPManualPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {
		
	}

}
