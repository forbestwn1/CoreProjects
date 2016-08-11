package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoMap extends HAPValueInfoContainer{

	public static final String ATTR_KEY = "key";
	 
	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP;	}

	@Override
	public void init(){
		super.init();
		this.updateBasicChild(ATTR_KEY, "name");
	}

}
