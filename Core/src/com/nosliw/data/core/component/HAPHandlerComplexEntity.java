package com.nosliw.data.core.component;

import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPDefinitionEntityInDomainComplex complexEntity, HAPDefinitionEntityInDomainComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
