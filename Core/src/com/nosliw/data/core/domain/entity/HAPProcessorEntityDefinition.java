package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPProcessorEntityDefinition {

	void processRoot(HAPIdEntityInDomain entityId, Object globalObj);

	void processAttribute(HAPIdEntityInDomain parentEntityId, String attrName, Object globalObj);

}
