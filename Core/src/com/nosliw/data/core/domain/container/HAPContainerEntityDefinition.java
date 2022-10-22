package com.nosliw.data.core.domain.container;

public abstract class HAPContainerEntityDefinition<T extends HAPElementContainerDefinition> extends HAPContainerEntity<T>{

	public HAPContainerEntityDefinition() {	}

	public HAPContainerEntityDefinition(String eleType, boolean isComplex) {
		super(eleType, isComplex);
	}

}
