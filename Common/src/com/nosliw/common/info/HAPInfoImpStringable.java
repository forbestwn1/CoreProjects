package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPInfoImpStringable extends HAPStringableValueEntity implements HAPInfo{

	@Override
	public Object getValue(String name) {		return this.getAtomicAncestorValue(name);	}

	@Override
	public void setValue(String name, Object value) {  this.updateAtomicChildObjectValue(name, value);  }

	@Override
	public Set<String> getNames() {  return this.getProperties(); }

}
