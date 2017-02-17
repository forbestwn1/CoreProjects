package com.nosliw.common.strvalue.valueinfo;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntityOptions extends HAPValueInfo implements HAPValueInfoEntityable{

	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final String OPTIONS = "options";
	
	private HAPValueInfoEntityOptions(){}

	public static HAPValueInfoEntityOptions build(){
		HAPValueInfoEntityOptions out = new HAPValueInfoEntityOptions();
		out.init();
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS);
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
		return this.getAtomicAncestorValueString(KEY);
	}
	
	public void setKeyName(String keyName){
		this.updateAtomicChildStrValue(KEY, keyName);
	}
	
	@Override
	public HAPValueInfoEntityOptions clone(){
		HAPValueInfoEntityOptions out = new HAPValueInfoEntityOptions();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public HAPStringableValue newValue() {		return null;	}
	
	public HAPStringableValue buildDefault(String optionsValue){
		return this.getOptionsValueInfo(optionsValue).newValue();
	}
	
	private HAPStringableValueMap getOptionsMap(){
		HAPStringableValueMap optionsValueInfo = (HAPStringableValueMap)this.getChild(HAPValueInfoEntityOptions.OPTIONS);
		return optionsValueInfo;
	}

	@Override
	public HAPValueInfo getPropertyInfo(String name) {
		return null;
	}
}
