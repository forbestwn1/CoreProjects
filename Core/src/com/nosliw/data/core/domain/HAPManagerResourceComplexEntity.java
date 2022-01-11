package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.resource.HAPResourceDefinition;
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
		HAPContextDomain domainContext = new HAPContextDomain();
		//build definition domain
		HAPResourceDefinition resourceDefinition = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, domainContext.getDefinitionDomain(), null);
		
		//process definition
		HAPContextProcessor processorContext = HAPUtilityDomain.createProcessContext(domainContext, resourceDefinition.getEntityId(), this.m_runtimeEnv); 
		HAPIdEntityInDomain exeEntityId = this.m_runtimeEnv.getComplexEntityManager().process(resourceDefinition.getEntityId(), processorContext);
		return new HAPResultExecutableEntityInDomain(exeEntityId, domainContext);
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {  return this.m_runtimeEnv;   }
	
}
