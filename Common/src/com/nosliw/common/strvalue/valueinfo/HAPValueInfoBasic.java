package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoBasic extends HAPValueInfo{

	public static final String ENTITY_PROPERTY_DEFAULTVALUE = "default";

	private HAPValueInfoBasic(){}
	
	public static HAPValueInfoBasic build(){
		HAPValueInfoBasic out = new HAPValueInfoBasic();
		out.init();
		return out;
	}

	@Override
	public HAPValueInfoBasic clone(){
		HAPValueInfoBasic out = new HAPValueInfoBasic();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC;	}

	@Override
	public HAPStringableValue buildDefault() {
		HAPStringableValue out = null;
		String defaultValue = this.getBasicAncestorValueString(HAPValueInfoBasic.ENTITY_PROPERTY_DEFAULTVALUE);
		if(defaultValue!=null){
			out = new HAPStringableValueBasic(defaultValue, this.getValueDataType());
		}
		return out;
	}
}
