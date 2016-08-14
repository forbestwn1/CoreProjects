package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoBasic extends HAPValueInfo{

	public static final String ATTR_DEFAULTVALUE = "default";

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
}
