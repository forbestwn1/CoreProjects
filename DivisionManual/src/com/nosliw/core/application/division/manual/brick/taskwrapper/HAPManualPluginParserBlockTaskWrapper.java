package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskWrapper extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockTaskWrapper(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockSimpleTaskWrapper.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
	}
}
