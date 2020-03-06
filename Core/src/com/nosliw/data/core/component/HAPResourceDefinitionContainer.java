package com.nosliw.data.core.component;

public abstract class HAPResourceDefinitionContainer extends HAPResourceDefinitionComplexImp{

	public abstract HAPComponent getElement(String name);
	
	public abstract HAPResourceDefinitionContainer cloneResourceDefinitionContainer();
	
	public void cloneToResourceDefinitionContainer(HAPResourceDefinitionContainer resourceDefinitionContainer) {
		this.cloneToComplexResourceDefinition(resourceDefinitionContainer);
	}
}
