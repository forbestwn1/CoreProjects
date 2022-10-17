package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbededWithIdExecutable;

public class HAPElementContainerExecutableWithId extends HAPElementContainerExecutable<HAPEmbededWithIdExecutable>{

	public HAPElementContainerExecutableWithId(HAPEmbededWithIdExecutable embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerExecutableWithId() {	}

	@Override
	public HAPElementContainerExecutableWithId cloneContainerElement() {
		HAPElementContainerExecutableWithId out = new HAPElementContainerExecutableWithId();
		this.cloneToInfoContainerElement(out);
		return out;
	}
}
