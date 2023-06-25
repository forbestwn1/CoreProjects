package com.nosliw.data.core.domain.entity;

import java.util.Set;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;

public interface HAPProcessorEntityDefinition {

	void process(HAPInfoEntityInDomainDefinition entityInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainDefinition parentEntityInfo, HAPDomainEntityDefinitionGlobal globalDomain);
	
}
