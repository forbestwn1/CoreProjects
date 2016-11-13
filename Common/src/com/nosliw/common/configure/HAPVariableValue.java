package com.nosliw.common.configure;

import com.nosliw.common.strvalue.HAPStringableValueAtomic;

public class HAPVariableValue extends HAPResolvableConfigureItem{

	public HAPVariableValue(String rawString) {
		super(new HAPStringableValueAtomic(rawString, "String"));
	}

	private HAPVariableValue(){ super(null); }
	
	@Override
	String getType() {		return HAPConfigureItem.VARIABLE;	}
	
	public String getValue(){  return this.getStringableValue().getStringValue(); }

	public HAPVariableValue clone(){
		HAPVariableValue out = new HAPVariableValue();
		out.cloneFrom(this);
		return out;
	}
}
