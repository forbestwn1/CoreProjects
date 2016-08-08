package com.nosliw.common.strvalue.propertyinfo;

import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntity extends HAPValueInfo{

	public static final String ATTR_CLASSNAME = "class";
	public static final String ATTR_MANDATORY = "mandatory";
	public static final String ATTR_PROPERTIES = "property";

	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY;	}

	@Override
	protected void init(){
		super.init();
		this.updateBasicChildValue(ATTR_MANDATORY, true);
	}
}
