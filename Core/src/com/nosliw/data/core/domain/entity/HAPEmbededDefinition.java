package com.nosliw.data.core.domain.entity;

public class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value, Object adapter) {
		super(value, adapter);
	}

	public HAPEmbededDefinition(Object value) {
		super(value, null);
	}

	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededDefinition out = new HAPEmbededDefinition();
		this.cloneToEmbeded(out);
		return out;
	}

}
