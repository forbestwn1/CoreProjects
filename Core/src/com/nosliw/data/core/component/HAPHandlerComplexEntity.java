package com.nosliw.data.core.component;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPDefinitionEntityInDomainComplex complexEntity, HAPDefinitionEntityInDomainComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
