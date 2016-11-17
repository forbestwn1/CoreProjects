package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoMap extends HAPValueInfoContainer{

	public static final String KEY = "key";
	
	private HAPValueInfoMap(){}
	
	public static HAPValueInfoMap build(){
		HAPValueInfoMap out = new HAPValueInfoMap();
		out.init();
		return out;
	}
	
	@Override
	public HAPValueInfoMap clone(){
		HAPValueInfoMap out = new HAPValueInfoMap();
		out.cloneFrom(this);
		return out;
	}
	
	public String getKey(){
		return this.getAtomicAncestorValueString(KEY);
	}
	
	public void setKey(String key){
		this.updateBasicChild(KEY, key);
	}
	
	@Override
	public void init(){
		super.init();
		this.updateBasicChild(KEY, "name");
	}

	@Override
	public HAPStringableValue buildDefault() {
		return new HAPStringableValueMap();
	}

}
