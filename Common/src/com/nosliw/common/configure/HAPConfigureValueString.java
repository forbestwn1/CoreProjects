package com.nosliw.common.configure;

import java.util.List;

import com.nosliw.common.strvalue.HAPStringableValueAtomic;

public class HAPConfigureValueString extends HAPResolvableConfigureItem implements HAPConfigureValue{
	public HAPConfigureValueString(String value){
		super(new HAPStringableValueAtomic(value));
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
	public <T> List<T> getListValue(Class<T> cs){  	return this.getStringableValue().getListValue(cs.getName()); }
	
	public HAPConfigureValueString clone(){
		HAPConfigureValueString out = new HAPConfigureValueString();
		out.cloneFrom(this);
		return out;
	}
}
