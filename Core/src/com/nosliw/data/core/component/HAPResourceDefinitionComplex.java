package com.nosliw.data.core.component;

import com.nosliw.data.core.resource.HAPResourceDefinition;

public interface HAPResourceDefinitionComplex extends HAPDefinitionComplex, HAPResourceDefinition{

	HAPChildrenComponentIdContainer getChildrenComponentId();

	void cloneToComplexResourceDefinition(HAPResourceDefinitionComplex complexEntity);
}
