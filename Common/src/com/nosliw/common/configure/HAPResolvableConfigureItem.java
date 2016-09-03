package com.nosliw.common.configure;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;

/*
 * wrapper for string content that need to be interpolated
 */
abstract class HAPResolvableConfigureItem extends HAPConfigureItem{
	
	private HAPStringableValueBasic m_value;
	
	public HAPResolvableConfigureItem(HAPStringableValueBasic value){
		this.setStringableValue(value);
	}

	protected void setStringableValue(HAPStringableValueBasic value){ this.m_value = value; }
	
	public String getStringContent(){ return this.m_value.getStringContent(); }
	
	public Object getValue(String type){
		if(this.m_value==null)   return null;
		return this.m_value.getValue(type);
	}
	
	public HAPStringableValueBasic getStringableValue(){ return this.m_value; }
	
	public boolean isResolved(){  return this.m_value.isResolved(); }
	
	public HAPInterpolateOutput resolve(Map<HAPInterpolateProcessor, Object> patternDatas){	return this.m_value.resolveByInterpolateProcessor(patternDatas);	}
	
	protected void cloneFrom(HAPResolvableConfigureItem configureItem){
		super.cloneFrom(configureItem);
		this.m_value = configureItem.m_value.clone();
	}
	
	@Override
	public String toString() {		return this.m_value.toString();	}
	
	@Override
	public String toStringValue(String format) {
		return this.m_value.toStringValue(format);
	}
}
