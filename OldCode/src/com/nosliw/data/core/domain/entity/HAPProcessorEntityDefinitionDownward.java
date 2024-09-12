package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPProcessorEntityDefinitionDownward {

	void processComplexRoot(HAPIdEntityInDomain entityId, Object globalObj);

	void processAttribute(HAPIdEntityInDomain parentEntityId, String attrName, Object globalObj);

}
