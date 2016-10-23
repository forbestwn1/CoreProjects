package com.nosliw.common.strvalue;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.resolve.HAPResolvableString;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPStringableValueObject extends HAPStringableValue{

	private HAPResolvableString m_strValue;
	
	private boolean m_sovled = false;
	
	public HAPStringableValueObject(String strValue){
		if(strValue!=null)		this.m_strValue = new HAPResolvableString(strValue);
	}
	
	abstract protected void parseStringValue(String strValue);
	
	@Override
	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas){	return this.m_strValue.resolveByPattern(patternDatas);	}
	@Override
	public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas){ return this.m_strValue.resolveByInterpolateProcessor(patternDatas);}
	
	@Override
	public boolean isResolved(){  return this.m_strValue.isResolved();  }

	@Override
	public String getStringableCategary() {  return HAPConstant.STRINGABLE_VALUECATEGARY_OBJECT;  }

	@Override
	public HAPStringableValue getChild(String name) {		return null;	}

	@Override
	public boolean isEmpty() {
		if(this.m_strValue==null)  return true;
		else	return this.m_strValue.isEmpty();
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	protected void cloneFrom(HAPStringableValueObject stringableValue){
		this.m_strValue = stringableValue.m_strValue.clone();
		this.m_sovled = stringableValue.m_sovled;
	}

	@Override
	protected HAPStringableValue cloneStringableValue() {
		return this.clone(this.getClass());
	}
	
	public <T extends HAPStringableValueObject> T clone(Class<T> cs){
		T out = null;
		try{
			out = cs.newInstance();
			out.cloneFrom(this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
}
