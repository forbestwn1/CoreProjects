package com.nosliw.common.configure;

import com.nosliw.common.strvalue.HAPStringableValueBasic;

public class HAPVariableValue extends HAPResolvableConfigureItem{

	public HAPVariableValue(String rawString) {
		super(new HAPStringableValueBasic(rawString, "String"));
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
