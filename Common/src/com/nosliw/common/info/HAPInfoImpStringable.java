package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPInfoImpStringable extends HAPStringableValueEntity implements HAPInfo{

	@Override
	public Object getValue(String name) {		return this.getAtomicAncestorValue(name);	}

	@Override
	public Object getValue(String name, Object defaultValue) {
		Object out = this.getValue(name);
		return out==null?defaultValue:out;
	}

	@Override
	public void setValue(String name, Object value) {  this.updateAtomicChildObjectValue(name, value);  }

	@Override
	public Set<String> getNames() {  return this.getProperties(); }

	@Override
	public HAPInfo cloneInfo() { return (HAPInfo)this.clone();	}

	@Override
	public HAPInfo cloneInfo(Set<String> excluded) {
		if(excluded==null||excluded.isEmpty())		 return this.cloneInfo();
		else {
			HAPInfoImpSimple out = new HAPInfoImpSimple();
			for(String name : this.getNames()) {
				if(!excluded.contains(name)) {
					out.setValue(name, this.getValue(name));
				}
			}
			return out;
		}
	}
}
