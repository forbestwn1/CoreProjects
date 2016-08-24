package com.nosliw.common.strvalue.basic;

import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateExpressionProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.utils.HAPConstant;

/*
 * stringable value which can be interpreted as differnt type (boolean, integer, array, map, object)
 */
public class HAPStringableValueBasic extends HAPStringableValue{

	private HAPResolvableString m_strValue;
	
	private String m_type;
	
	private Object m_value;
	
	private boolean m_sovled = false;
	
	public HAPStringableValueBasic(String strValue, String type){
		this(new HAPResolvableString(strValue), type);
	}

	public HAPStringableValueBasic(HAPResolvableString strValue, String type){
		this.setStringContent(strValue, type);
	}

	public HAPStringableValueBasic(String strValue, String type, String defaultValue){
		if(strValue!=null)		this.setStringContent(new HAPResolvableString(strValue), type);
		else	this.setStringContent(new HAPResolvableString(defaultValue), type);
	}
	
	public HAPStringableValueBasic(String strValue){
		this(strValue, null);
	}

	public HAPStringableValueBasic(){}
	
	@Override
	public String getStringableCategary(){		return HAPConstant.CONS_STRINGABLE_VALUECATEGARY_BASIC;	}
	
	@Override
	public boolean isEmpty(){
		if(this.m_value!=null)  return false;
		if(this.m_strValue==null)  return true;
		else	return this.m_strValue.isEmpty();
	}
	
	public String getStringContent(){  return this.m_strValue.getValue();  }
	public String getType(){  return this.m_type;  }

	public void setStringContent(String content){ 
		this.m_strValue.setValue(content);
		this.m_sovled = false;
	}
	private void setStringContent(HAPResolvableString strValue, String type){
		this.m_strValue = strValue;
		this.m_type = type;
		this.m_sovled = false;
	}
	
	public void setValue(Object value){
		HAPStringableValueBasic stringValue = HAPStringableValueUtility.valueToString(value);
		if(value!=null){
			this.m_strValue = stringValue.m_strValue;
			this.m_type = stringValue.m_type;
			this.m_value = value;
			this.m_sovled = true;
		}
	}
	
	@Override
	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas){	return this.m_strValue.resolveByPattern(patternDatas);	}
	@Override
	public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateExpressionProcessor, Object> patternDatas){ return this.m_strValue.resolveByInterpolateProcessor(patternDatas);}

	
	public boolean isValueResolved(){		return this.m_sovled;	}
	
	@Override
	public boolean isStringResolved(){  return this.m_strValue.isStringResolved();  }
	
	public Object getValue(){
		if(this.m_sovled)  return this.m_value;
		if(this.m_type!=null)  return this.getValue(m_type);
		return this.m_strValue;
	}
	
	public Object getValue(String type){
		if(type==null)  return this.getValue();
		
		//whether already resolved
		if(this.m_sovled && type.equals(this.m_type))  return this.m_value;
		
		//if m_value is not solved, return null
		if(!this.m_strValue.isStringResolved())  return null;
		
		Object out = HAPStringableValueUtility.stringToValue(m_strValue.getValue(), type);
		if(out!=null)  this.resolved(out, type);
		return out;
	}
	
	private void resolved(Object value, String type){
		this.m_sovled = true;
		this.m_value = value;
		this.m_type = type;
	}
	
	private void unresolved(){
		this.m_sovled = false;
		this.m_value = null;
		this.m_type = null;
	}
	
	public String getStringValue() {	return this.m_strValue.getValue();	}
	public Boolean getBooleanValue() {		return (Boolean)this.getValue(HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_BOOLEAN);	}
	public Integer getIntegerValue() {		return (Integer)this.getValue(HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_INTEGER);	}
	public Float getFloatValue() {		return (Float)this.getValue(HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_FLOAT);	}
	public List<String> getListValue(){		return (List<String>)this.getValue(HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_ARRAY);	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {
		super.buildFullJsonMap(jsonMap, typeJsonMap, format);
		jsonMap.put(HAPAttributeConstant.ATTR_STRINGABLEVALUE_TYPE, this.m_type);
		jsonMap.put(HAPAttributeConstant.ATTR_STRINGABLEVALUE_STRINGVALUE, this.m_strValue.toString());
		jsonMap.put(HAPAttributeConstant.ATTR_STRINGABLEVALUE_RESOLVED, String.valueOf(this.m_sovled));
		jsonMap.put(HAPAttributeConstant.ATTR_STRINGABLEVALUE_VALUE, this.m_value==null?null : this.m_value.toString());
	}
	
	@Override
	protected String buildJson(String format){
		return this.m_strValue.toString();
	}
	
	protected void cloneFrom(HAPStringableValueBasic stringableValue){
		this.m_strValue = stringableValue.m_strValue.clone();
		this.m_type = stringableValue.m_type;
		this.m_value = stringableValue.m_value;
		this.m_sovled = stringableValue.m_sovled;
	}

	@Override
	public HAPStringableValueBasic clone(){
		HAPStringableValueBasic out = new HAPStringableValueBasic();
		out.cloneFrom(this);
		return out;
	}

	@Override
	public HAPStringableValue getChild(String name) {
		return null;
	}
}
