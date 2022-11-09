package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityExecutableNormalEntity extends HAPAttributeEntityExecutableNormal<HAPEmbededExecutableWithEntity>{

	public HAPAttributeEntityExecutableNormalEntity(String name, HAPEmbededExecutableWithEntity value) {
		super(name, value);
	}

	public HAPAttributeEntityExecutableNormalEntity() {}

	@Override
	public HAPAttributeEntityExecutableNormalEntity cloneEntityAttribute() {
		HAPAttributeEntityExecutableNormalEntity out = new HAPAttributeEntityExecutableNormalEntity();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
