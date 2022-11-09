package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithEntity;

public class HAPElementContainerExecutableWithEntity1 extends HAPElementContainerExecutable<HAPEmbededExecutableWithEntity>{

	public HAPElementContainerExecutableWithEntity1(HAPEmbededExecutableWithEntity embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerExecutableWithEntity1() {	}

	@Override
	public HAPElementContainerExecutableWithEntity1 cloneContainerElement() {
		HAPElementContainerExecutableWithEntity1 out = new HAPElementContainerExecutableWithEntity1();
		this.cloneToContainerElement(out);
		return out;
	}
}
