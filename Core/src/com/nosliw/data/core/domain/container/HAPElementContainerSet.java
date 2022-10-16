package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbeded;

public abstract class HAPElementContainerSet<T extends HAPEmbeded> extends HAPElementContainer<T>{

	public HAPElementContainerSet() {}
	
	public HAPElementContainerSet(T embeded, String elementId) {
		super(embeded, elementId);
	}
}
