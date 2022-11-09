package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPAttributeEntityDefinitionValue extends HAPAttributeEntityDefinition<HAPEmbededDefinitionWithValue>{

	public HAPAttributeEntityDefinitionValue(String name, HAPEmbededDefinitionWithValue value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE, name, value);
	}

	public HAPAttributeEntityDefinitionValue() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE);
	}
	
	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPAttributeEntityDefinitionValue cloneEntityAttribute() {
		HAPAttributeEntityDefinitionValue out = new HAPAttributeEntityDefinitionValue();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionValue attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue(this.getValue().cloneEmbeded());
	}
}
