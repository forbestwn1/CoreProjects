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
		this.updateAtomicChildStrValue(KEY, key);
	}
	
	@Override
	public void init(){
		super.init();
		this.updateAtomicChildStrValue(KEY, "name");
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_MAP);
	}

	@Override
	public HAPStringableValue newValue() {
		return new HAPStringableValueMap();
	}

}
