package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPConfigureEntityInDomainComplex;

public interface HAPProcessorComplexEntity {

	void process(HAPConfigureEntityInDomainComplex complexEntityInfo, HAPConfigureEntityInDomainComplex parentComplexEntityInfo, HAPContextProcessor processContext);
	
}
