package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoMap extends HAPValueInfoContainer{

	public static final String ENTITY_PROPERTY_KEY = "key";
	
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
		return this.getBasicAncestorValueString(ENTITY_PROPERTY_KEY);
	}
	
	public void setKey(String key){
		this.updateBasicChild(ENTITY_PROPERTY_KEY, key);
	}
	
	@Override
	public String getCategary() {		return HAPConstant.STRINGALBE_VALUEINFO_MAP;	}

	@Override
	public void init(){
		super.init();
		this.updateBasicChild(ENTITY_PROPERTY_KEY, "name");
	}

	@Override
	public HAPStringableValue buildDefault() {
		return new HAPStringableValueMap();
	}

}
