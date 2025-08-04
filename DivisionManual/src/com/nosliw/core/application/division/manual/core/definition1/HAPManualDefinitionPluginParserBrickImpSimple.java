package com.nosliw.core.application.division.manual.core.definition1;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualDefinitionPluginParserBrickImpSimple extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpSimple(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
	}
}
