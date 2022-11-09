package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPAttributeEntityDefinitionId extends HAPAttributeEntityDefinition<HAPEmbededDefinitionWithId>{

	public HAPAttributeEntityDefinitionId(String name, HAPEmbededDefinitionWithId entityId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE, name, entityId);
	}

	public HAPAttributeEntityDefinitionId() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE);
	}
	
	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPAttributeEntityDefinitionId cloneEntityAttribute() {
		HAPAttributeEntityDefinitionId out = new HAPAttributeEntityDefinitionId();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionId attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue(this.getValue().cloneEmbeded());
	}
}
