package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbeded;

public abstract class HAPInfoContainerElementSet<T extends HAPEmbeded> extends HAPInfoContainerElement<T>{

	public HAPInfoContainerElementSet() {}
	
	public HAPInfoContainerElementSet(T embeded) {
		super(embeded);
	}
}
