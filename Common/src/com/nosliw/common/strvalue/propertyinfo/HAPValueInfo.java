package com.nosliw.common.strvalue.propertyinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String ATTR_TYPE = "type";
	
	abstract public String getCategary();

	@Override
	protected void init(){
		super.init();
		this.updateBasicChild(ATTR_TYPE, HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING);
	}
	
}
