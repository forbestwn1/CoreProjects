package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPLiterateArray  implements HAPLiterateDef{

	@Override
	public String getName() {	return HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_ARRAY;	}

	@Override
	public Object stringToValue(String strValue, String subType) {
		List<Object> out = new ArrayList<Object>();
		if(strValue==null)   return out;
		
		int startIndex = strValue.indexOf(HAPConstantShared.SEPERATOR_ARRAYSTART);
		int endIndex = strValue.lastIndexOf(HAPConstantShared.SEPERATOR_ARRAYEND);
		
		String arrayStr = strValue;
		if(startIndex!=-1 && endIndex!=-1){
			arrayStr = strValue.substring(startIndex+1, endIndex);
		}
		
		String type = null;
		String type1 = null;
		if(HAPLiterateManager.getInstance().isValidType(subType)){
			type = subType;
		}
		else{
			type = HAPConstantShared.STRINGABLE_ATOMICVALUETYPE_OBJECT;
			type1 = subType;
		}
		
		String[] elesArray = HAPUtilityNamingConversion.parseElements(arrayStr);
		for(String eleStr : elesArray){
			String a = eleStr;
			if(a.startsWith("\""))  a = eleStr.substring(1, a.length()-1);
			out.add(HAPLiterateManager.getInstance().stringToValue(a, type, type1));
		}
		return out;  
	}
	
	@Override
	public String valueToString(Object value) {  
		StringBuffer arrayStr = new StringBuffer();
		List<String> elesStr = new ArrayList<String>();
		
		Collection collectionValue = (Collection)value;
		Iterator it = collectionValue.iterator();
		while(it.hasNext()){
			Object eleObj = it.next();
			elesStr.add("\""+HAPLiterateManager.getInstance().valueToString(eleObj)+"\"");
		}
		
		arrayStr.append(HAPUtilityNamingConversion.cascadeElementArray(elesStr.toArray(new String[0])));
		return arrayStr.toString(); 
		
	}

	@Override
	public List<Class> getObjectClasses() {  
		List<Class> out = new ArrayList<Class>(); 
		out.add(ArrayList.class);
		out.add(List.class);
		out.add(Set.class);
		out.add(HashSet.class);
		return out;
	}

	@Override
	public String getSubTypeByType(Type valueType) {
		return HAPUtilityBasic.getParameterizedType(valueType);
	}
}
