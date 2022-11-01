package com.nosliw.data.core.domain;

public abstract class HAPAttributeEntityDefinition<T> extends HAPAttributeEntity<T>{

	public HAPAttributeEntityDefinition(String type, String name, T value) {
		super(type, name, value);
	}

	public HAPAttributeEntityDefinition(String type) {
		super(type);
	}
}
