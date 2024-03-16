package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpSimple extends HAPPluginParserEntityImp{

	public HAPPluginParserEntityImpSimple(HAPIdBrickType entityTypeId, Class<? extends HAPManualEntity> entityClass,
			HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}
}
