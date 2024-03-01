package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpSimple extends HAPPluginParserEntityImp{

	public HAPPluginParserEntityImpSimple(HAPIdEntityType entityTypeId, Class<? extends HAPManualEntity> entityClass,
			HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}
}
