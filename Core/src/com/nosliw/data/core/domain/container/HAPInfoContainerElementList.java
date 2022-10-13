package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbeded;

public interface HAPInfoContainerElementList<T extends HAPEmbeded> extends HAPInfoContainerElement<T>{

	public static final String INDEX = "index";

	int getIndex();

}
