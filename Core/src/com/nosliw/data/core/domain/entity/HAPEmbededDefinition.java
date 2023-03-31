package com.nosliw.data.core.domain.entity;

public abstract class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value, String entityType, Object adapter, boolean isComplex) {
		super(value, entityType, adapter, isComplex);
	}

}
