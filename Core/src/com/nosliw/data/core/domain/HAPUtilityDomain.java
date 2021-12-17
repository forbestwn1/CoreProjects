package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

	public static HAPContextProcessor createProcessContext(HAPContextDomain domainContext, HAPIdEntityInDomain entityId, HAPRuntimeEnvironment runtimeEnv) {
		HAPInfoDefinitionEntityInDomain entityInfo = domainContext.getDefinitionDomain().getEntityInfo(entityId);
		HAPContextProcessor out = new HAPContextProcessor(domainContext, entityInfo.getLocalBaseReference(), runtimeEnv);
		return out;
	}
	
}
