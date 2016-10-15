package com.nosliw.common.strvalue;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.constant.HAPConstantUtility;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.resolve.HAPResolvableString;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;

public class HAPStringableValueUtility {

	public static Set<String> getExpectedAttributesInEntity(Class entityClass){
		Set<String> out = new HashSet<String>();
		HAPValueInfoEntity info = HAPValueInfoManager.getInstance().getEntityValueInfoByClass(entityClass);
		if(info!=null){
			out = info.getEntityProperties();
		}
		else{
			out = HAPConstantUtility.getAttributes(entityClass);
		}
		return out;
	}
	
	public static boolean isStringableValueEmpty(HAPStringableValue value){
		if(value==null)  return true;
		return value.isEmpty();
	}
	
	public static HAPStringableValueComplex newStringableValueComplex(String type){
		HAPStringableValueComplex out = null;
		if(HAPConstant.STRINGABLE_VALUECATEGARY_ENTITY.equals(type)){
			out = new HAPStringableValueEntity();
		}
		else if(HAPConstant.STRINGABLE_VALUECATEGARY_LIST.equals(type)){
			out = new HAPStringableValueList();
		}
		else if(HAPConstant.STRINGABLE_VALUECATEGARY_MAP.equals(type)){
			out = new HAPStringableValueMap();
		}
		return out;
	}
	
	public static Object stringToValue(String strValue, String type){
		Object out = null;
		try{
			if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_STRING))		out = strValue;
			else if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_BOOLEAN))		out = Boolean.valueOf(strValue);
			else if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_INTEGER))  out = Integer.valueOf(strValue);
			else if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_FLOAT))   out = Float.valueOf(strValue);
			else if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_ARRAY)){
				//if the string value represent array, build array instead
				if(strValue.startsWith(HAPConstant.SEPERATOR_ARRAYSTART) && strValue.endsWith(HAPConstant.SEPERATOR_ARRAYEND)){
					//value is array
					out = HAPNamingConversionUtility.parseElements(strValue.substring(1, strValue.length()-1));
				}
			}
			else if(type.equals(HAPConstant.STRINGABLE_BASICVALUETYPE_MAP)){
				
			}
			else{
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public static HAPStringableValueBasic valueToString(Object value){
		HAPStringableValueBasic out = null;
		if(value==null)  return null;
		
		if(value.getClass().equals(String.class))  out = new HAPStringableValueBasic(new HAPResolvableString(value.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_STRING);
		else if(value.getClass().equals(Boolean.class))  out = new HAPStringableValueBasic(new HAPResolvableString(value.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_BOOLEAN);
		else if(value.getClass().equals(Integer.class))  out = new HAPStringableValueBasic(new HAPResolvableString(value.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_INTEGER);
		else if(value.getClass().equals(Float.class))  out = new HAPStringableValueBasic(new HAPResolvableString(value.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_FLOAT);
		else if(value.getClass().equals(List.class)){
			StringBuffer listStr = new StringBuffer();
			listStr.append(HAPConstant.SEPERATOR_ARRAYSTART);
			listStr.append(HAPNamingConversionUtility.cascadeElement(((List<String>)value).toArray(new String[0])));
			listStr.append(HAPConstant.SEPERATOR_ARRAYEND);
			out = new HAPStringableValueBasic(new HAPResolvableString(listStr.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_ARRAY);
		}
		return out;
	}
}
