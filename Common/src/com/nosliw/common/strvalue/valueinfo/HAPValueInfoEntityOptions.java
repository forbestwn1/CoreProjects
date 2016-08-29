package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntityOptions extends HAPValueInfo{

	public static final String ENTITY_PROPERTY_KEY = "key";
	public static final String ENTITY_PROPERTY_VALUE = "value";
	public static final String ENTITY_PROPERTY_OPTIONS = "options";
	
	private HAPValueInfoEntityOptions(){}

	public static HAPValueInfoEntityOptions build(){
		HAPValueInfoEntityOptions out = new HAPValueInfoEntityOptions();
		out.init();
		return out;
	}

	public HAPValueInfo getOptionsValueInfo(String value){
		HAPStringableValueEntity optionsValueInfo = (HAPStringableValueEntity)this.getChild(HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS);
		return (HAPValueInfo)optionsValueInfo.getChild(value);
	}
	
	public String getKeyName(){
		return this.getBasicAncestorValueString(ENTITY_PROPERTY_KEY);
	}
	
	@Override
	public HAPValueInfoEntityOptions clone(){
		HAPValueInfoEntityOptions out = new HAPValueInfoEntityOptions();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS;	}

	@Override
	public HAPStringableValue buildDefault() {		return null;	}
	
	public HAPStringableValue buildDefault(String optionsValue){
		return this.getOptionsValueInfo(optionsValue).buildDefault();
	}
}
