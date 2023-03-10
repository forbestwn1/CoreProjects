package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;

public interface HAPProcessorEntityDefinition {

	void process(HAPInfoEntityInDomainDefinition entityInfo, Object adapter, HAPInfoEntityInDomainDefinition parentEntityInfo, HAPContextProcessor processContext);
	
}
