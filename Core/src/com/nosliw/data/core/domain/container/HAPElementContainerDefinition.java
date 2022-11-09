package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPElementContainerDefinition<T extends HAPEmbededDefinition> extends HAPElementContainer<T>{

	public HAPElementContainerDefinition(T embeded, String elementId) {
		super(embeded, elementId);
	}

	public HAPElementContainerDefinition() {	}

	@Override
	public HAPElementContainerDefinition cloneContainerElement() {
		HAPElementContainerDefinition out = new HAPElementContainerDefinition();
		this.cloneToContainerElement(out);
		return out;
	}
}
