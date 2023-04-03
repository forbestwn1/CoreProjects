package com.nosliw.data.core.domain.entity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPAttributeEntityExecutableNormal extends HAPAttributeEntityExecutable<HAPEmbededExecutable> implements HAPExecutable{

	public HAPAttributeEntityExecutableNormal(String name, HAPEmbededExecutable value, boolean isComplex) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, value, isComplex);
	}

	public HAPAttributeEntityExecutableNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}

	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableNormal attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((HAPEmbededExecutable)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityExecutableNormal out = new HAPAttributeEntityExecutableNormal();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
