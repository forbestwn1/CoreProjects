package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityExecutableNormalId extends HAPAttributeEntityExecutableNormal<HAPEmbededExecutableWithId>{

	public HAPAttributeEntityExecutableNormalId(String name, HAPEmbededExecutableWithId value) {
		super(name, value);
	}

	public HAPAttributeEntityExecutableNormalId() {	}

	@Override
	public HAPAttributeEntityExecutableNormalId cloneEntityAttribute() {
		HAPAttributeEntityExecutableNormalId out = new HAPAttributeEntityExecutableNormalId();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
}
