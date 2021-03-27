package com.nosliw.data.core.component;

public class HAPResourceDefinitionContainerElementEntityImpComponent extends HAPComponentImp implements HAPResourceDefinitionContainerElementEntity{

	public HAPResourceDefinitionContainerElementEntityImpComponent() {}
	
	public HAPResourceDefinitionContainerElementEntityImpComponent(String id) {     
		super(id);
	}
	
	@Override
	public String getType() {  return HAPResourceDefinitionContainerElement.TYPE_ENTITY;  }

	protected void cloneToResourceDefinitionContainerElementEntityImpComponent(HAPResourceDefinitionContainerElementEntityImpComponent to) {
		super.cloneToComponent(to);
	}
	
	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPResourceDefinitionContainerElementEntityImpComponent out = new HAPResourceDefinitionContainerElementEntityImpComponent();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out);
		return out;
	}

	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		// TODO Auto-generated method stub
		return null;
	}

}
