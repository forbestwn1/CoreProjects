package com.nosliw.data.core.component;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPManualBrickComplex complexEntity, HAPManualBrickComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
