package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityDefinitionNormalValue extends HAPAttributeEntityDefinitionNormal<HAPEmbededDefinitionWithValue>{

	public HAPAttributeEntityDefinitionNormalValue(String name, HAPEmbededDefinitionWithValue value) {
		super(name, value);
	}

	public HAPAttributeEntityDefinitionNormalValue() {}
	
	@Override
	public HAPAttributeEntityDefinitionNormalValue cloneEntityAttribute() {
		HAPAttributeEntityDefinitionNormalValue out = new HAPAttributeEntityDefinitionNormalValue();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
