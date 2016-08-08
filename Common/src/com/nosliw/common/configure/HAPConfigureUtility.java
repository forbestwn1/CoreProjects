package com.nosliw.common.configure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateExpressionProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;
import com.nosliw.common.utils.HAPConstant;

public class HAPConfigureUtility {

	private static String KEY_VARIABLE_GLOBAL = "global";

	//the interpolate processor
	private static HAPInterpolateExpressionProcessor m_interpolateProcessor = new HAPInterpolateExpressionProcessor(HAPConstant.CONS_SEPERATOR_VARSTART, HAPConstant.CONS_SEPERATOR_VAREND){
		@Override
		public String processIterpolate(String expression, Object object) {
			Object[] arrayObj = (Object[]) object;
			boolean resursive = (Boolean)arrayObj[1];
			List<HAPConfigureImp> configures = new ArrayList<HAPConfigureImp>();
			if(arrayObj[0] instanceof HAPConfigureImp)  configures.add((HAPConfigureImp)arrayObj[0]);
			else if(arrayObj[0] instanceof List)   configures = (List<HAPConfigureImp>)arrayObj[0];
			
			String out = null;
			for(HAPConfigureImp configure : configures){
				//through variable
				HAPVariableValue value = configure.getVariableValue(expression);
				if(value!=null){
					//found variable
					if(value.isResolved())  return value.getValue();
					else{
						if(resursive){
							resolveConfigureItem(value, resursive);
							if(value.isResolved())  return value.getValue();
						}
					}
				}
			}
			return out;
		}
	};

	public static String isGlobalVariable(String name){
		if(!name.startsWith(KEY_VARIABLE_GLOBAL)){
			return null;
		}
		else{
			return name.substring(KEY_VARIABLE_GLOBAL.length()+1);
		}
	}
	
	/*
	 * resolve resolvable item (configure value or variable value)
	 */
	public static HAPInterpolateOutput resolveConfigureItem(HAPResolvableConfigureItem resolvableItem, final boolean resursive){
		Map<HAPInterpolateExpressionProcessor, Object> interpolateDatas = new LinkedHashMap<HAPInterpolateExpressionProcessor, Object>();
		
		List<HAPConfigureImp> configures = new ArrayList<HAPConfigureImp>();
		configures.add(resolvableItem.getParent());
		HAPConfigureImp baseConfigure = resolvableItem.getRootParent().getBaseConfigure();
		if(baseConfigure!=null)  configures.add(baseConfigure);
		interpolateDatas.put(m_interpolateProcessor, new Object[]{configures, new Boolean(resursive)});
		
		HAPInterpolateOutput out = resolvableItem.resolve(interpolateDatas);
		return out;
	}
	
	/*
	 * create StringAbleValue from string value from configure file
	 * it follow the format of type:value or value if the value type is String  
	 */
	public static HAPStringableValueBasic getStringableValue(String strValue){
		String[] parts = HAPNamingConversionUtility.parsePartlInfos(strValue);
		String type = HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING;
		String value = null; 
		if(parts.length>=2){
			value = parts[1];
			type = parts[0];
		}
		else{
			value = strValue;
		}
		return new HAPStringableValueBasic(value, type);
	}
}
