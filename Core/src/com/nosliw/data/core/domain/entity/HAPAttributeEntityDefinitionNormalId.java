package com.nosliw.data.core.domain.entity;

public class HAPAttributeEntityDefinitionNormalId extends HAPAttributeEntityDefinitionNormal<HAPEmbededDefinitionWithId>{

	public HAPAttributeEntityDefinitionNormalId(String name, HAPEmbededDefinitionWithId entityId) {
		super(name, entityId);
	}

	public HAPAttributeEntityDefinitionNormalId() {	}
	
	@Override
	public HAPAttributeEntityDefinitionNormalId cloneEntityAttribute() {
		HAPAttributeEntityDefinitionNormalId out = new HAPAttributeEntityDefinitionNormalId();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
