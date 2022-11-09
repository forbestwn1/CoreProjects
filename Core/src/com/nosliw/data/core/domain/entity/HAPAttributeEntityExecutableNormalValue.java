package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityExecutableNormalValue extends HAPAttributeEntityExecutableNormal<HAPEmbededExecutableWithValue>{

	public HAPAttributeEntityExecutableNormalValue(String name, HAPEmbededExecutableWithValue value) {
		super(name, value);
	}

	public HAPAttributeEntityExecutableNormalValue() {}

	@Override
	public HAPAttributeEntityExecutableNormalValue cloneEntityAttribute() {
		HAPAttributeEntityExecutableNormalValue out = new HAPAttributeEntityExecutableNormalValue();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
}
