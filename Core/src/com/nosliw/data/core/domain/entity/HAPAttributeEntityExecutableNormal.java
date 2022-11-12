package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPExecutable;

public abstract class HAPAttributeEntityExecutableNormal<T extends HAPEmbededExecutable> extends HAPAttributeEntityExecutable<T> implements HAPExecutable{

	public HAPAttributeEntityExecutableNormal(String name, T value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, value);
	}

	public HAPAttributeEntityExecutableNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}

	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableNormal attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((T)this.getValue().cloneEmbeded());
	}
}
