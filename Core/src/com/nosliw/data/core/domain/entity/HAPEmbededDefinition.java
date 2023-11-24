package com.nosliw.data.core.domain.entity;

import java.util.Set;

public class HAPEmbededDefinition extends HAPEmbeded{

	public HAPEmbededDefinition() {}
	
	public HAPEmbededDefinition(Object value) {
		super(value);
	}

	public HAPEmbededDefinition(Object value, String valueType) {
		super(value, valueType);
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
