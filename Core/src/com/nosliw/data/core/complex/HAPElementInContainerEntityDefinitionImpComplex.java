package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPDefinitionEntityComponent;

public class HAPElementInContainerEntityDefinitionImpComplex extends HAPDefinitionEntityComplexImp implements HAPElementInContainerEntityDefinition{

	public HAPElementInContainerEntityDefinitionImpComplex() {}
	
	public HAPElementInContainerEntityDefinitionImpComplex(String id) {
		super(id);
	}
	
	@Override
	public String getElementType() {  return HAPElementInContainerEntityDefinition.TYPE_ENTITY;  }

	protected void cloneToResourceDefinitionContainerElementEntityImpComponent(HAPElementInContainerEntityDefinitionImpComplex to, boolean cloneValueStructure) {
		super.cloneToComponent(to, cloneValueStructure);
	}
	
	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
		HAPElementInContainerEntityDefinitionImpComplex out = new HAPElementInContainerEntityDefinitionImpComplex();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out, true);
		return out;
	}

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {  return this.cloneDefinitionEntityElementInContainer(); }

}
