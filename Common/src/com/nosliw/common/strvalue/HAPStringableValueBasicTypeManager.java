package com.nosliw.common.strvalue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPStringableValueBasicTypeManager {

	private static Map<String, HAPStringableValueBasicType> m_typesByName;
	private static Map<Class, HAPStringableValueBasicType> m_typesByClass;
	
	static{
		m_typesByName = new LinkedHashMap<String, HAPStringableValueBasicType>();
		m_typesByClass = new LinkedHashMap<Class, HAPStringableValueBasicType>();
		
		register(new HAPStringableValueBasicType(){
			public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_STRING;	}
			public Object stringToValue(String strValue) {  return strValue;  }
			public String valueToString(Object value) {  return value.toString(); }
			public Class getObjectClass() {  return String.class; }
		});

		register(new HAPStringableValueBasicType(){
			public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_BOOLEAN;	}
			public Object stringToValue(String strValue) {  return Boolean.valueOf(strValue);  }
			public String valueToString(Object value) {  return value.toString(); }
			public Class getObjectClass() {  return Boolean.class; }
		});
		
		register(new HAPStringableValueBasicType(){
			public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_INTEGER;	}
			public Object stringToValue(String strValue) {  return Integer.valueOf(strValue);  }
			public String valueToString(Object value) {  return value.toString(); }
			public Class getObjectClass() {  return Integer.class; }
		});

		register(new HAPStringableValueBasicType(){
			public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_FLOAT;	}
			public Object stringToValue(String strValue) {  return Float.valueOf(strValue);  }
			public String valueToString(Object value) {  return value.toString(); }
			public Class getObjectClass() {  return Float.class; }
		});
		
		register(new HAPStringableValueBasicType(){
			public String getName() {	return HAPConstant.STRINGABLE_BASICVALUETYPE_ARRAY;	}
			public Object stringToValue(String strValue) {  
				//if the string value represent array, build array instead
				if(strValue.startsWith(HAPConstant.SEPERATOR_ARRAYSTART) && strValue.endsWith(HAPConstant.SEPERATOR_ARRAYEND)){
					//value is array
					return HAPNamingConversionUtility.parseElements(strValue.substring(1, strValue.length()-1));
				}
				return null;  
			}
			public String valueToString(Object value) {  
				StringBuffer arrayStr = new StringBuffer();
				arrayStr.append(HAPConstant.SEPERATOR_ARRAYSTART);
				arrayStr.append(HAPNamingConversionUtility.cascadeElement(((List<String>)value).toArray(new String[0])));
				arrayStr.append(HAPConstant.SEPERATOR_ARRAYEND);
				return arrayStr.toString(); 
				
			}
			public Class getObjectClass() {  return Integer.class; }
		});

	}
	
	private static void register(HAPStringableValueBasicType typeObj){
		m_typesByName.put(typeObj.getName(), typeObj);
		m_typesByClass.put(typeObj.getObjectClass(), typeObj);
	}
	
	public static Object stringToValue(String strValue, String type){
		Object out = null;
		HAPStringableValueBasicType typeObj = m_typesByName.get(type);
		if(typeObj!=null){
			out = typeObj.stringToValue(strValue);
		}
		return out;
	}
	
	public static String getType(Object value){
		return m_typesByClass.get(value.getClass()).getName();
	}
	
	public static String valueToString(Object value){
		HAPStringableValueBasicType typeObj = m_typesByClass.get(value.getClass());
		return typeObj.valueToString(typeObj);
	}
	
	public static boolean isBasicType(String type){
		if(HAPBasicUtility.isStringEmpty(type))  return false;
		else   return m_typesByName.keySet().contains(type);
	}
	
	
	interface HAPStringableValueBasicType{
		
		String getName();
		
		Object stringToValue(String strValue);
		
		String valueToString(Object value);
		
		Class getObjectClass();
	}
}
