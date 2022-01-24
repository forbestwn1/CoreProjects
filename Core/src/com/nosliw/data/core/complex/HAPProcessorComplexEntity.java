package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomainComplex;

public interface HAPProcessorComplexEntity {

	void process(HAPInfoDefinitionEntityInDomainComplex complexEntityInfo, HAPInfoDefinitionEntityInDomainComplex parentComplexEntityInfo, HAPContextProcessor processContext);
	
}
