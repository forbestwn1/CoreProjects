package com.nosliw.data.core.domain.entity;

public class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value) {
		super(value);
	}

	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededDefinition out = new HAPEmbededDefinition();
		this.cloneToEmbeded(out);
		return out;
	}

}
