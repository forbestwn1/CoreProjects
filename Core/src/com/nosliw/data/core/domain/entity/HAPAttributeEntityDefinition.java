package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityDefinition extends HAPAttributeEntity<HAPEmbededDefinition>{

	public HAPAttributeEntityDefinition(String name, HAPEmbededDefinition embeded, HAPInfoValueType valueTypeInfo) {
		super(name, embeded, valueTypeInfo);
	}

	public HAPAttributeEntityDefinition() {}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinition attr) {
		super.cloneToEntityAttribute(attr);
		attr.setValue((HAPEmbededDefinition)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityDefinition out = new HAPAttributeEntityDefinition();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
