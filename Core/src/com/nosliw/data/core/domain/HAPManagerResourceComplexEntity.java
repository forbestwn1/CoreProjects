package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerResourceComplexEntity {

	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerResourceComplexEntity(
			HAPRuntimeEnvironment runtimeEnv
			) {
		this.m_runtimeEnv = runtimeEnv;
	}

	public HAPResultExecutableEntityInDomain getExecutableComplexEntity(HAPResourceId resourceId) {
		return HAPUtilityDomain.getResourceExecutableComplexEntity(resourceId, m_runtimeEnv);
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {  return this.m_runtimeEnv;   }
	
}
