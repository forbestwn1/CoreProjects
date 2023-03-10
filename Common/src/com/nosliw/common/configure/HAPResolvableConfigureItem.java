package com.nosliw.common.configure;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.interpolate.HAPInterpolateOutput;

/*
 * wrapper for string content that need to be interpolated
 */
abstract class HAPResolvableConfigureItem extends HAPConfigureItem{
	
	private HAPStringableValueAtomic m_value;
	
	public HAPResolvableConfigureItem(HAPStringableValueAtomic value){
		this.setStringableValue(value);
	}

	protected void setStringableValue(HAPStringableValueAtomic value){ this.m_value = value; }
	
	public String getStringContent(){ return this.m_value.getStringContent(); }
	
	public Object getValue(String type, String subType){
		if(this.m_value==null)   return null;
		return this.m_value.getValue(type, subType);
	}
	
	public HAPStringableValueAtomic getStringableValue(){ return this.m_value; }
	
	public boolean isResolved(){  return this.m_value.isResolved(); }
	
	public HAPInterpolateOutput resolve(Map<HAPInterpolateProcessor, Object> patternDatas){	return this.m_value.resolveByInterpolateProcessor(patternDatas);	}
	
	protected void cloneFrom(HAPResolvableConfigureItem configureItem){
		super.cloneFrom(configureItem);
		this.m_value = (HAPStringableValueAtomic)configureItem.m_value.clone();
	}
	
	@Override
	public String toString() {		return this.m_value.toString();	}
	
	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return this.m_value.toStringValue(format);
	}
}
