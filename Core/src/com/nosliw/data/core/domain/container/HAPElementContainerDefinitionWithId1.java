package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithId;

public class HAPElementContainerDefinitionWithId1 extends HAPElementContainerDefinition<HAPEmbededDefinitionWithId>{

	public HAPElementContainerDefinitionWithId1(HAPEmbededDefinitionWithId embededWithId, String elementId) {
		super(embededWithId, elementId);
	}

	public HAPElementContainerDefinitionWithId1() {	}

	@Override
	public HAPElementContainerDefinitionWithId1 cloneContainerElement() {
		HAPElementContainerDefinitionWithId1 out = new HAPElementContainerDefinitionWithId1();
		this.cloneToContainerElement(out);
		return out;
	}

	public HAPElementContainerDefinitionWithId1 cloneContainerElementDefinitionWithId() {	return this.cloneContainerElement();	}
}
