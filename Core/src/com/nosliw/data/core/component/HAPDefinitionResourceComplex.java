package com.nosliw.data.core.component;

import com.nosliw.data.core.resource.HAPResourceDefinition;

//a complex resource definition that contains attachment, context data, child resource 
public interface HAPDefinitionResourceComplex extends HAPDefinitionEntityComplex, HAPResourceDefinition{

	HAPContainerChildResource getChildrenResource();

	void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity);
}
