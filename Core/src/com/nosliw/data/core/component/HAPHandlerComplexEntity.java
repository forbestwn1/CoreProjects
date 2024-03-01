package com.nosliw.data.core.component;

import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public interface HAPHandlerComplexEntity {

	void process(HAPManualEntityComplex complexEntity, HAPManualEntityComplex parentEntity, HAPConfigureParentRelationComplex parentInfo);
	
}
