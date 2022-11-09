package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPAttributeEntityDefinitionNormal<T extends HAPEmbededDefinition> extends HAPAttributeEntityDefinition<T>{

	public HAPAttributeEntityDefinitionNormal(String name, T entityId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, entityId);
	}

	public HAPAttributeEntityDefinitionNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}
	
	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionNormal attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((T)this.getValue().cloneEmbeded());
	}
}
