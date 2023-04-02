package com.nosliw.data.core.domain.entity;

public abstract class HAPAttributeEntityDefinition<T> extends HAPAttributeEntity<T>{

	public HAPAttributeEntityDefinition(String type, String name, T value, boolean isComplex) {
		super(type, name, value, isComplex);
	}

	public HAPAttributeEntityDefinition(String type) {
		super(type);
	}
}
