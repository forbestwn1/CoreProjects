package com.nosliw.common.configure;

import java.util.List;

public class HAPConfigureValueString extends HAPResolvableConfigureItem implements HAPConfigureValue{
	public HAPConfigureValueString(String value){
		super(HAPConfigureUtility.getStringableValue(value));
	}
	
	private HAPConfigureValueString(){ super(null); }
	
	@Override
	String getType() {	return HAPConfigureItem.VALUE;	}
	
	@Override
	public String getStringValue() {	return this.getStringableValue().getStringValue();	}

	@Override
	public Boolean getBooleanValue() {  return this.getStringableValue().getBooleanValue(); }

	@Override
	public Integer getIntegerValue() {  return this.getStringableValue().getIntegerValue();  }

	@Override
	public Float getFloatValue() {		return this.getStringableValue().getFloatValue();  }

	@Override
	public List<String> getListValue(){  	return this.getStringableValue().getListValue(); }
	
	public HAPConfigureValueString clone(){
		HAPConfigureValueString out = new HAPConfigureValueString();
		out.cloneFrom(this);
		return out;
	}
}
