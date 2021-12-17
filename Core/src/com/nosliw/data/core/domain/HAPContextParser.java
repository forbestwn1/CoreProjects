package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextParser {

	//runtime
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	//local ref base for 
	private HAPLocalReferenceBase m_localRefBase;

	//definition domain
	private HAPDomainDefinitionEntity m_definitionDomain;

	public HAPContextParser(HAPDomainDefinitionEntity definitionDomain, HAPRuntimeEnvironment runtimeEnv, HAPLocalReferenceBase localRefBase) {
		this.m_definitionDomain = definitionDomain;
		this.m_runtimeEnv = runtimeEnv;
		this.m_localRefBase = localRefBase;
	}
	
	public HAPDomainDefinitionEntity getDefinitionDomain() {    return this.m_definitionDomain;      }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {     return this.m_runtimeEnv;      }
	
	public HAPLocalReferenceBase getLocalReferenceBase() {    return this.m_localRefBase;    }
}
