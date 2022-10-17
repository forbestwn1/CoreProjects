package com.nosliw.data.core.domain.container;

import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;
import com.nosliw.data.core.domain.HAPExpandable;

public abstract class HAPElementContainerDefinition<T extends HAPEmbededWithIdDefinition> extends HAPElementContainer<T>  implements HAPExpandable{

	public HAPElementContainerDefinition(T embeded, String elementId) {
		super(embeded, elementId);
	}

	public HAPElementContainerDefinition() {	}

}
