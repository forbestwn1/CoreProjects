package com.nosliw.data.core.component;

public class HAPElementContainerResourceDefinitionEntityImpComponent extends HAPComponentImp implements HAPElementContainerResourceDefinitionEntity{

	public HAPElementContainerResourceDefinitionEntityImpComponent() {}
	
	public HAPElementContainerResourceDefinitionEntityImpComponent(String id) {     
		super(id);
	}
	
	@Override
	public String getType() {  return HAPElementContainerResourceDefinition.TYPE_ENTITY;  }

	protected void cloneToResourceDefinitionContainerElementEntityImpComponent(HAPElementContainerResourceDefinitionEntityImpComponent to, boolean cloneValueStructure) {
		super.cloneToComponent(to, cloneValueStructure);
	}
	
	@Override
	public HAPElementContainerResourceDefinition cloneResourceDefinitionContainerElement() {
		HAPElementContainerResourceDefinitionEntityImpComponent out = new HAPElementContainerResourceDefinitionEntityImpComponent();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out, true);
		return out;
	}

	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

}
