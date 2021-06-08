package com.nosliw.data.core.component;

import com.nosliw.data.core.resource.HAPResourceDefinition;

//a complex resource definition that contains attachment, context data, child resource 
public interface HAPDefinitionResourceComplex extends HAPDefinitionEntityComplex, HAPResourceDefinition{

	//all external resource reffered by this complex resource, for debug purpose
	HAPContainerChildReferenceResource getChildrenReferencedResource();

	void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity, boolean cloneValueStructure);
}
