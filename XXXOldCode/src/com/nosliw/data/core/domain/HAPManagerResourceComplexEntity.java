package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerResourceComplexEntity {

	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerResourceComplexEntity(
			HAPRuntimeEnvironment runtimeEnv
			) {
		this.m_runtimeEnv = runtimeEnv;
	}

	public HAPExecutablePackage getExecutableComplexEntity(HAPResourceIdSimple resourceId) {
		return this.m_runtimeEnv.getDomainEntityExecutableManager().getExecutablePackage(resourceId);
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {  return this.m_runtimeEnv;   }
	
}
