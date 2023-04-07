package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPElementContainerDefinition extends HAPElementContainer<HAPEmbededDefinition>{

	public HAPElementContainerDefinition(HAPEmbededDefinition embeded, String elementId) {
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
