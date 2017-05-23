package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPInfoImpStringable extends HAPStringableValueEntity implements HAPInfo{

	@Override
	public String getValue(String name) {		return this.getAtomicAncestorValueString(name);	}

	@Override
	public void setValue(String name, String value) {  this.updateAtomicChildStrValue(name, value);  }

	@Override
	public Set<String> getNames() {  return this.getProperties(); }

}
