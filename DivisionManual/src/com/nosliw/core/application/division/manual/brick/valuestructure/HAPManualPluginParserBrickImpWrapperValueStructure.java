package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpWrapperValueStructure extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpWrapperValueStructure(HAPIdBrickType brickTypeId,
			Class<? extends HAPManualDefinitionBrick> brickClass, HAPManualManagerBrick manualDivisionEntityMan,
			HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
		// TODO Auto-generated constructor stub
	}

}
