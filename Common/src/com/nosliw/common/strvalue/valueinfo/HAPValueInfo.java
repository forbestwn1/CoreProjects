package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String ATTR_TYPE = "type";
	public static final String ATTR_REFERENCE = "ref";
	
	abstract public String getCategary();
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	@Override
	public void init(){
		super.init();
		this.updateBasicChild(ATTR_TYPE, HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING);
	}
	
}
