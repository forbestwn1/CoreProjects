package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPAttributeEntityDefinitionSimple extends HAPAttributeEntityDefinition<HAPEmbededDefinitionWithId>{

	public HAPAttributeEntityDefinitionSimple(String name, HAPEmbededDefinitionWithId value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE, name, value);
	}

	public HAPAttributeEntityDefinitionSimple() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE);
	}
	
	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPAttributeEntityDefinitionSimple cloneEntityAttribute() {
		HAPAttributeEntityDefinitionSimple out = new HAPAttributeEntityDefinitionSimple();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionSimple attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue(this.getValue().cloneEmbeded());
	}
}
