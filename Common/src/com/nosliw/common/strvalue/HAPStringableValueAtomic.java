package com.nosliw.common.strvalue;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.resolve.HAPResolvableString;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;

/*
 * stringable value which can be interpreted as differnt type (boolean, integer, array, map, object)
 */
public class HAPStringableValueAtomic extends HAPStringableValue{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String SUBTYPE = "subType";
	
	@HAPAttribute
	public static String STRINGVALUE = "stringValue";
	
	//when resolved is true, it means that the value is resolved (value data and its type and subType)
	@HAPAttribute
	public static String RESOLVED = "resolved";

	@HAPAttribute
	public static String VALUE = "atomic_value";

	
	private HAPResolvableString m_strValue;
	
	private HAPLiterateType m_type;
	
	private Object m_value;
	
	private boolean m_sovled = false;
	
	public HAPStringableValueAtomic(String strValue, String type, String subType, String defaultValue){
		if(strValue!=null)		this.setStringContent(new HAPResolvableString(strValue), type, subType);
		else	this.setStringContent(new HAPResolvableString(defaultValue), type, subType);
	}

	public HAPStringableValueAtomic(String strValue, String type, String subType){
		this(new HAPResolvableString(strValue), type, subType);
	}

	public HAPStringableValueAtomic(HAPResolvableString strValue, String type, String subType){
		this.setStringContent(strValue, type, subType);
	}
	
	public HAPStringableValueAtomic(String strValue){
		this(strValue, null, null);
	}

	public HAPStringableValueAtomic(){}

	public static HAPStringableValueAtomic buildFromObject(Object o){
		HAPStringableValueAtomic out = new HAPStringableValueAtomic();
		out.setValue(o);
		return out;
	}
	
	@Override
	public boolean isEmpty(){
		if(this.m_value!=null)  return false;
		if(this.m_strValue==null)  return true;
		else	return this.m_strValue.isEmpty();
	}
	
	public String getStringContent(){
		if(this.m_sovled)			this.buildStringValueByValue();		
		if(this.m_strValue==null)  return null;
		else return this.m_strValue.getValue();  
	}
	
	public void setStringContent(String content){ 
		this.m_strValue.setValue(content);
		this.m_sovled = false;
	}
	private void setStringContent(HAPResolvableString strValue, String type, String subType){
		this.m_strValue = strValue;
		if(type!=null)		this.m_type = new HAPLiterateType(type, subType);
		this.m_sovled = false;
	}
	

	public HAPLiterateType getLiterateType(){
		if(this.m_type==null)		this.buildLiterateTypeByValue();
		return this.m_type;
	}

	private void buildLiterateTypeByValue(){
		if(this.m_value!=null)			this.m_type = HAPLiterateManager.getInstance().getLiterateType(this.m_value);
		else this.m_type = null;
	}
	
	private void buildStringValueByValue(){
		if(this.m_value!=null)		this.m_strValue = new HAPResolvableString(HAPLiterateManager.getInstance().valueToString(this.m_value), true);
		else  this.m_strValue = null;
	}
	
	public void setValue(Object value){
		this.m_value = value;
		this.m_sovled = true;
		this.buildLiterateTypeByValue();
		this.buildStringValueByValue();
	}
	
	@Override
	protected HAPInterpolateOutput resolveValueByPattern(Map<String, Object> patternDatas){	return this.m_strValue.resolveByPattern(patternDatas);	}
	@Override
	protected HAPInterpolateOutput resolveValueByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas){ return this.m_strValue.resolveByInterpolateProcessor(patternDatas);}

	public boolean isValueResolved(){		return this.m_sovled;	}
	
	@Override
	public boolean isResolved(){  return this.m_strValue.isResolved();  }
	
	public Object getValue(){
		if(this.m_sovled)  return this.m_value;
		if(this.m_type!=null)  return this.getValue(m_type.getType(), m_type.getSubType());
		return this.m_strValue.toString();
	}
	
	public Object getValue(String type, String subType){
		Object out = null;
		HAPLiterateType literateType = new HAPLiterateType(type, subType);
		if(literateType.equals(this.m_type) && this.m_sovled){
			out = this.m_value;
		}
		else{
			if(this.m_sovled)  this.getStringContent();
			out = this.calValue(literateType);
			this.resolved(out, type, subType);
		}
		return out;
	}
	
	private Object calValue(HAPLiterateType literateType){
		if(this.m_strValue==null)  return null;
		else return HAPLiterateManager.getInstance().stringToValue(m_strValue.getValue(), literateType);
	}
	
	private void resolved(Object value, String type, String subType){
		this.m_sovled = true;
		this.m_value = value;
		this.m_type = new HAPLiterateType(type, subType);
	}
	
	private void unresolved(){
		this.m_sovled = false;
		this.m_value = null;
		this.m_type = null;
	}
	
	public String getStringValue() {	return this.getStringContent();	}
	public Boolean getBooleanValue() {		return (Boolean)this.getValue(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_BOOLEAN, null);	}
	public Integer getIntegerValue() {		return (Integer)this.getValue(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);	}
	public Float getFloatValue() {		return (Float)this.getValue(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_FLOAT, null);	}
	public <T> List<T> getListValue(String subTypeName){
		return (List<T>)this.getValue(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_ARRAY, subTypeName);	
	}
	public <T> T getObjectValue(Class<T> cs){
		return (T)this.getValue(HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT, cs.getName());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_type!=null)   jsonMap.put(TYPE, this.m_type.getType());
		if(this.m_type!=null)   jsonMap.put(SUBTYPE, this.m_type.getSubType());
		jsonMap.put(STRINGVALUE, this.m_strValue.toString());
		jsonMap.put(RESOLVED, String.valueOf(this.m_sovled));
		jsonMap.put(VALUE, this.m_value==null?null : this.getStringContent());
	}
	
	@Override
	protected String buildJson(){		return this.getStringContent();	}
	
	@Override
	protected String buildLiterate(){  return this.getStringContent(); }
	
	protected void cloneFrom(HAPStringableValueAtomic stringableValue){
		this.m_strValue = stringableValue.m_strValue.clone();
		this.m_type = stringableValue.m_type;
		this.m_sovled = stringableValue.m_sovled;
		this.m_value = HAPLiterateManager.getInstance().clone(stringableValue.m_value);
	}

	@Override
	public HAPStringableValueAtomic cloneStringableValue(){
		HAPStringableValueAtomic out = new HAPStringableValueAtomic();
		out.cloneFrom(this);
		return out;
	}

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPStringableValueAtomic){
			HAPStringableValueAtomic value = (HAPStringableValueAtomic)obj;
			if(HAPUtilityBasic.isEquals(value.m_strValue, this.m_strValue)){
				if(value.m_type==null || this.m_type==null)  out = true;
				else  out = HAPUtilityBasic.isEquals(value.m_type, this.m_type);
			}
		}
		return out;
	}
	
	@Override
	public HAPStringableValue getChild(String name) {
		return null;
	}
	
	@Override
	public String getStringableStructure(){		return HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ATOMIC;	}
	
	@Override
	public String toString(){		return this.buildLiterate();	}
}
