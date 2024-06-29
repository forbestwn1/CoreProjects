package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionElementInLibrary;
import com.nosliw.core.application.common.script.HAPElementInLibraryScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPUtilityScriptExpressionParser;

public class HAPPluginProcessorBlockScriptExpressionElementInLibrary extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBlockScriptExpressionElementInLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPElementInLibraryScriptExpression exe = ((HAPBlockScriptExpressionElementInLibrary)blockExe).getValue();;
		HAPManualScriptExpressionLibraryElement def = ((HAPManualBlockScriptExpressionElementInLibrary)blockDef).getValue();
		
		//entity info
		def.cloneToEntityInfo(exe);
		
		//expression
		HAPExpressionScript scriptExpression = HAPUtilityScriptExpressionParser.parseDefinitionExpression(def.getExpression(), null, exe.getDataExpressionGroup(), processContext.getRuntimeEnv().getDataExpressionParser());
		exe.setExpression(scriptExpression);
		
		//process expression group
		
		
		
		
		
	}
}
