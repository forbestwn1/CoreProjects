package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;

public interface HAPProcessorComplexEntity {

	void process(HAPInfoEntityInDomainDefinition complexEntityInfo, Object adapter, HAPInfoEntityInDomainDefinition parentComplexEntityInfo, HAPContextProcessor processContext);
	
}
