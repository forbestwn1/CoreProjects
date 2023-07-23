package com.nosliw.data.core.domain.entity;

import java.util.Set;

public class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value) {
		super(value);
	}

	public Set<HAPInfoAdapterDefinition> getDefinitionAdapters(){   return (Set)this.getAdapters();     }
	public HAPInfoAdapterDefinition getDefinitionAdapter(String name) {	return (HAPInfoAdapterDefinition)this.getAdapter(name);	}
	
	@Override
	public HAPEmbeded cloneEmbeded() {
		HAPEmbededDefinition out = new HAPEmbededDefinition();
		this.cloneToEmbeded(out);
		return out;
	}

}
