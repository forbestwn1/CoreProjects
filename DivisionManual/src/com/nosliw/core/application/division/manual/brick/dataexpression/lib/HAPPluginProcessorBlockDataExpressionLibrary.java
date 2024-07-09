package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockDataExpressionLibrary extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockDataExpressionLibrary(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockDataExpressionLibrary.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		
	}

}
