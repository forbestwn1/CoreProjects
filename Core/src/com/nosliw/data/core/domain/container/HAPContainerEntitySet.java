package com.nosliw.data.core.domain.container;

public abstract class HAPContainerEntitySet<T extends HAPInfoContainerElementSet> extends HAPContainerEntity<T>{

	public HAPContainerEntitySet() {	}
	
	public HAPContainerEntitySet(String eleType) {
		super(eleType);
	}
}