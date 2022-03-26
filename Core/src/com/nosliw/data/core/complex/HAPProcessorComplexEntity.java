package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomain;

public interface HAPProcessorComplexEntity {

	void process(HAPInfoDefinitionEntityInDomain complexEntityInfo, Object adapter, HAPInfoDefinitionEntityInDomain parentComplexEntityInfo, HAPContextProcessor processContext);
	
}
