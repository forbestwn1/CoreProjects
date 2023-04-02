package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPAttributeEntityDefinitionNormal extends HAPAttributeEntityDefinition<HAPEmbededDefinition>{

	public HAPAttributeEntityDefinitionNormal(String name, HAPEmbededDefinition embeded, boolean isComplex) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, embeded, isComplex);
	}

	public HAPAttributeEntityDefinitionNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionNormal attr) {
		super.cloneToEntityAttribute(attr);
		attr.setValue((HAPEmbededDefinition)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityDefinitionNormal out = new HAPAttributeEntityDefinitionNormal();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
