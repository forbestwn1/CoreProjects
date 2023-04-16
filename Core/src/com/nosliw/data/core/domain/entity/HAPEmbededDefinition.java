package com.nosliw.data.core.domain.entity;

public class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value, HAPInfoAdapter adapter) {
		super(value, adapter);
	}

	public HAPEmbededDefinition(Object value) {
		super(value, null);
	}

	public HAPInfoAdapter getAdapterEntity() {    return (HAPInfoAdapter)this.getAdapter();     }
	
	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededDefinition out = new HAPEmbededDefinition();
		this.cloneToEmbeded(out);
		return out;
	}

}
