package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.division.manua.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manua.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.xxx.application1.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPPluginProcessorBlockScriptExpressionLibrary extends HAPManualPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {
		
	}

}
