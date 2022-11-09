package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;

public class HAPAttributeEntityDefinitionContainer extends HAPAttributeEntityDefinition<HAPContainerEntityDefinition>{

	public HAPAttributeEntityDefinitionContainer(String name, HAPContainerEntityDefinition value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER, name, value);
	}

	public HAPAttributeEntityDefinitionContainer() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER);
	}
	
	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPAttributeEntityDefinitionContainer cloneEntityAttribute() {
		HAPAttributeEntityDefinitionContainer out = new HAPAttributeEntityDefinitionContainer();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityDefinitionContainer attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((HAPContainerEntityDefinition)this.getValue().cloneContainerEntity());
	}
}
