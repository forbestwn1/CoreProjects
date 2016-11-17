package com.nosliw.common.strvalue.valueinfo;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
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

	public Set<String> getOptionsKey(){
		HAPStringableValueMap optionsValueInfo = this.getOptionsMap();
		return optionsValueInfo.getKeys();
	}
	
	public HAPValueInfo getOptionsValueInfo(String value){
		return (HAPValueInfo)getOptionsMap().getChild(value);
	}

	public void setOptionsValueInfo(String value, HAPValueInfo valueInfo){
		this.getOptionsMap().updateChild(value, valueInfo);
	}
	
	public String getKeyName(){
		return this.getAtomicAncestorValueString(ENTITY_PROPERTY_KEY);
	}
	
	public void setKeyName(String keyName){
		this.updateBasicChild(ENTITY_PROPERTY_KEY, keyName);
	}
	
	@Override
	public HAPValueInfoEntityOptions clone(){
		HAPValueInfoEntityOptions out = new HAPValueInfoEntityOptions();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public HAPStringableValue buildDefault() {		return null;	}
	
	public HAPStringableValue buildDefault(String optionsValue){
		return this.getOptionsValueInfo(optionsValue).buildDefault();
	}
	
	private HAPStringableValueMap getOptionsMap(){
		HAPStringableValueMap optionsValueInfo = (HAPStringableValueMap)this.getChild(HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS);
		return optionsValueInfo;
	}
}
