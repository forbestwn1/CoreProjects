package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPAttributeEntityExecutableContainer extends HAPAttributeEntityExecutable<HAPContainerEntityExecutable> implements HAPExecutable{

	public HAPAttributeEntityExecutableContainer(String name, HAPContainerEntityExecutable value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER, name, value);
	}

	public HAPAttributeEntityExecutableContainer() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER);
	}

	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPAttributeEntityExecutableContainer cloneEntityAttribute() {
		HAPAttributeEntityExecutableContainer out = new HAPAttributeEntityExecutableContainer();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableContainer attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((HAPContainerEntityExecutable)this.getValue().cloneContainerEntity());
	}
}
