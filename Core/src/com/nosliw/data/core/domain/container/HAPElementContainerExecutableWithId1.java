package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithId;

public class HAPElementContainerExecutableWithId1 extends HAPElementContainerExecutable<HAPEmbededExecutableWithId>{

	public HAPElementContainerExecutableWithId1(HAPEmbededExecutableWithId embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerExecutableWithId1() {	}

	@Override
	public HAPElementContainerExecutableWithId1 cloneContainerElement() {
		HAPElementContainerExecutableWithId1 out = new HAPElementContainerExecutableWithId1();
		this.cloneToContainerElement(out);
		return out;
	}
}
