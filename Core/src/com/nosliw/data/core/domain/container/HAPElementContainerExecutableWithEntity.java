package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbededWithEntityExecutable;

public class HAPElementContainerExecutableWithEntity extends HAPElementContainerExecutable<HAPEmbededWithEntityExecutable>{

	public HAPElementContainerExecutableWithEntity(HAPEmbededWithEntityExecutable embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerExecutableWithEntity() {	}

	@Override
	public HAPElementContainerExecutableWithEntity cloneContainerElement() {
		HAPElementContainerExecutableWithEntity out = new HAPElementContainerExecutableWithEntity();
		this.cloneToInfoContainerElement(out);
		return out;
	}
}
