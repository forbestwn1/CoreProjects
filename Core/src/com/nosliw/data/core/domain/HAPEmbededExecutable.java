package com.nosliw.data.core.domain;

import com.nosliw.data.core.runtime.HAPExecutable;

public abstract class HAPEmbededExecutable extends HAPEmbeded implements HAPExecutable{

	public HAPEmbededExecutable() {}
	
	public HAPEmbededExecutable(Object entity, String entityType, boolean isComplex) {
		super(entity, entityType, isComplex);
	}

}
