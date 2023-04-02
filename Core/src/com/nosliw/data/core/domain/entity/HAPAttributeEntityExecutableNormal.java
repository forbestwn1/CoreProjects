package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPAttributeEntityExecutableNormal<T extends HAPEmbededExecutable> extends HAPAttributeEntityExecutable<T> implements HAPExecutable{

	public HAPAttributeEntityExecutableNormal(String name, T value, boolean isComplex) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, value, isComplex);
	}

	public HAPAttributeEntityExecutableNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}

	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableNormal attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((T)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityExecutableNormal out = new HAPAttributeEntityExecutableNormal();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
