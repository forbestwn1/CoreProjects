package com.nosliw.common.strvalue.basic;

import java.util.List;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.constant.HAPConstantUtility;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.pattern.HAPPatternManager;
import com.nosliw.common.strvalue.valueinfo.HAPEntityValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPStringableValueUtility {

	public static Set<String> getExpectedAttributesInEntity(Class entityClass){
		Set<String> out = new HashSet<String>();
		HAPValueInfoEntity info = HAPValueInfoManager.getEntityValueInfoByClass(entityClass);
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
	
	public static HAPInterpolateOutput resolveByPatterns(String content, Map<String, Object> patternDatas){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		out.setOutput(content);
		if(content!=null){
			for(String patternName : patternDatas.keySet()){
				HAPInterpolateOutput result = resolveByPattern(out.getOutput(), patternName, patternDatas.get(patternName));
				out.setOutput(result.getOutput());
				out.addUnsolved(result.getUnsolved());
			}
		}
		return out;
	}
	
	public static HAPInterpolateOutput resolveByPattern(String content, String patternName, Object data){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		if(content!=null){
			out = (HAPInterpolateOutput)HAPPatternManager.getInstance().getPatternProcessor(patternName).parse(content, data);
		}
		return out;
	}
	
	
	public static HAPInterpolateOutput resolveByInterpolateProcessors(String content, Map<HAPInterpolateProcessor, Object> interpolateDatas){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		out.setOutput(content);
		if(content!=null){
			for(HAPInterpolateProcessor processor : interpolateDatas.keySet()){
				HAPInterpolateOutput result = resolveByInterpolateProcessor(out.getOutput(), processor, interpolateDatas.get(processor));
				out.setOutput(result.getOutput());
				out.addUnsolved(result.getUnsolved());
			}
		}
		return out;
	}

	public static HAPInterpolateOutput resolveByInterpolateProcessor(String content, HAPInterpolateProcessor processor, Object data){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		if(content!=null){
			out = processor.processExpression(content, data);
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
					HAPSegmentParser valueSegs = new HAPSegmentParser(strValue.substring(1, strValue.length()-1), HAPConstant.SEPERATOR_ELEMENT);
					out = Arrays.asList(valueSegs.getSegments());
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
			List<String> listValue = (List<String>)value;
			for(int i=0; i<listValue.size(); i++){
				listStr.append(listValue.get(i));
				if(i<listValue.size()-1)   listStr.append(HAPConstant.SEPERATOR_ELEMENT);  
			}
			listStr.append(HAPConstant.SEPERATOR_ARRAYEND);
			out = new HAPStringableValueBasic(new HAPResolvableString(listStr.toString(), true), HAPConstant.STRINGABLE_BASICVALUETYPE_ARRAY);
		}
		return out;
	}
	
}
