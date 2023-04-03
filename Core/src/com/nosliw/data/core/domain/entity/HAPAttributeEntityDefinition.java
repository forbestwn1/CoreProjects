package com.nosliw.data.core.domain.entity;

public abstract class HAPAttributeEntityDefinition<T> extends HAPAttributeEntity<T>{

	public HAPAttributeEntityDefinition(String type, String name, T value, HAPInfoValueType valueTypeInfo) {
		super(type, name, value, valueTypeInfo);
	}

	public HAPAttributeEntityDefinition(String type) {
		super(type);
	}
}
