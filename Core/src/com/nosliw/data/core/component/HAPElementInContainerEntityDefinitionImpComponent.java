package com.nosliw.data.core.component;

import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinition;

public class HAPElementInContainerEntityDefinitionImpComponent extends HAPDefinitionEntityComponentImp implements HAPElementInContainerEntityDefinition{

	public HAPElementInContainerEntityDefinitionImpComponent() {}
	
	public HAPElementInContainerEntityDefinitionImpComponent(String id) {
		super(id);
	}
	
	@Override
	public String getElementType() {  return HAPElementInContainerEntityDefinition.TYPE_ENTITY;  }

	protected void cloneToResourceDefinitionContainerElementEntityImpComponent(HAPElementInContainerEntityDefinitionImpComponent to, boolean cloneValueStructure) {
		super.cloneToComponent(to, cloneValueStructure);
	}
	
	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
		HAPElementInContainerEntityDefinitionImpComponent out = new HAPElementInContainerEntityDefinitionImpComponent();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out, true);
		return out;
	}

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {  return this.cloneDefinitionEntityElementInContainer(); }

}
