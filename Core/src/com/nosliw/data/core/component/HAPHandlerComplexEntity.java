package com.nosliw.data.core.component;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
