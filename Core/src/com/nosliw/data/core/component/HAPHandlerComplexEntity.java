package com.nosliw.data.core.component;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPInfoParentEntity;

public interface HAPHandlerComplexEntity {

	void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPInfoParentEntity parentInfo);
	
}
