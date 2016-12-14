package com.nosliw.common.strvalue;

import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.nosliw.common.constant.HAPConstantUtility;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.resolve.HAPResolvableString;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPXMLUtility;

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
		if(HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY.equals(type)){
			out = new HAPStringableValueEntity();
		}
		else if(HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST.equals(type)){
			out = new HAPStringableValueList();
		}
		else if(HAPConstant.STRINGABLE_VALUESTRUCTURE_MAP.equals(type)){
			out = new HAPStringableValueMap();
		}
		return out;
	}
	
	public static void updateBasicProperty(Element element, HAPStringableValueEntity entity){
		Map<String, String> propertyAttrs = HAPXMLUtility.getAllAttributes(element);
		entity.updateAtomicChildrens(propertyAttrs);
	}
}
