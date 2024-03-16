package com.nosliw.data.core.component;

import com.nosliw.core.application.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPManualEntityComplex complexEntity, HAPManualEntityComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
