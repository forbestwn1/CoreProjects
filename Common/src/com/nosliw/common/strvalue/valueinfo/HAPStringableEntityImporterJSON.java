package com.nosliw.common.strvalue.valueinfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.strvalue.basic.HAPAttributeConstant;
import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;
import com.nosliw.common.strvalue.basic.HAPStringableValueComplex;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.basic.HAPStringableValueList;
import com.nosliw.common.strvalue.basic.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;

public class HAPStringableEntityImporterJSON {

	public static HAPStringableValueEntity readEntityFromJson(JSONObject jsonObjEntity, String entityType, HAPValueInfoManager valueInfoMan){
		HAPStringableValueEntity out = processEntityValue(jsonObjEntity, (HAPValueInfoEntity)valueInfoMan.getValueInfo(entityType), valueInfoMan);
		return out;
	}
	
	private static HAPStringableValueEntity processEntityValue(JSONObject jsonObjEntity, HAPValueInfoEntity entityValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueEntity entity = entityValueInfo.newEntity();

		if(jsonObjEntity!=null){
			JSONObject propertiesJsonObj = jsonObjEntity;
			String jsonFormat = getJsonFormat(jsonObjEntity);
			if(HAPConstant.CONS_SERIALIZATION_JSON_FULL.equals(jsonFormat))  propertiesJsonObj = jsonObjEntity.optJSONObject(HAPAttributeConstant.ATTR_STRINGABLEVALUE_PROPERTIES);

			Set<String> entityOptionsProperties = new HashSet<String>();
			
			for(String property : entityValueInfo.getEntityProperties()){
				HAPValueInfo propertyValueInfo = entityValueInfo.getPropertyInfo(property);
				String propertyCategary = propertyValueInfo.getCategary();
				if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary))  entityOptionsProperties.add(property);
				else{
					HAPStringableValue entityProperty = readPropertyValueOfEntity(propertiesJsonObj, propertyValueInfo, valueInfoMan);
					if(entityProperty!=null)			entity.updateChild(property, entityProperty);
				}
			}
			
			for(String propertyName : entityOptionsProperties){
				HAPValueInfoEntityOptions propertyValueInfo = (HAPValueInfoEntityOptions)entityValueInfo.getPropertyInfo(propertyName).getSolidValueInfo();
				Object propertyObj = propertiesJsonObj.opt(propertyName);
				String keyValue = entity.getBasicAncestorValueString(propertyValueInfo.getKeyName());
				HAPStringableValue entityProperty = processEntityOptionsValue(propertyObj, keyValue, propertyValueInfo, valueInfoMan);
				if(entityProperty!=null)			entity.updateChild(propertyName, entityProperty);
			}
		}
		return HAPValueInfoUtility.validateStringableValueEntity(entityValueInfo, entity);
	}
	
	private static HAPStringableValue readPropertyValueOfEntity(JSONObject propertiesJsonObj, HAPValueInfo propertyValueInfo, HAPValueInfoManager valueInfoMan){
		String propertyName = propertyValueInfo.getName();
		
		HAPStringableValue out = null;
		try{
			HAPValueInfo propertyInfo = propertyValueInfo.getSolidValueInfo();
			String propertyCategary = propertyInfo.getCategary();
			Object propertyObj = propertiesJsonObj.opt(propertyName);
			if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(propertyCategary)){
				out = processListValue(propertyObj, (HAPValueInfoList)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(propertyCategary)){
				out = processMapValue(propertyObj, (HAPValueInfoMap)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(propertyCategary)){
				out = processEntityValue((JSONObject)propertyObj, (HAPValueInfoEntity)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary)){
				throw new Exception();
			}
			else{
				out = processBasicValue(propertyObj, (HAPValueInfoBasic)propertyInfo, valueInfoMan);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPStringableValueMap processMapValue(Object mapObj, HAPValueInfoMap mapValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueMap map = new HAPStringableValueMap();
		
		if(mapObj!=null){
			JSONObject mapPropertiesJsonObj = null;
			String jsonFormat = getJsonFormat((JSONObject)mapObj);
			if(HAPConstant.CONS_SERIALIZATION_JSON_FULL.equals(jsonFormat))  mapPropertiesJsonObj = ((JSONObject)mapObj).optJSONObject(HAPAttributeConstant.ATTR_STRINGABLEVALUE_ELEMENTS);
			else    mapPropertiesJsonObj = (JSONObject)mapObj;
			
			HAPValueInfo childInfo = mapValueInfo.getChildValueInfo().getSolidValueInfo();
			String childCategary = childInfo.getCategary();

			Iterator<String> it = mapPropertiesJsonObj.keys();
			while(it.hasNext()){
				String key = it.next();
				Object keyValue = mapPropertiesJsonObj.opt(key);
				HAPStringableValue mapElementValue = null;
				if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
					mapElementValue = processBasicValue(keyValue, (HAPValueInfoBasic)childInfo, valueInfoMan);
				}		
				else{
					mapElementValue = processComplexValue(keyValue, childInfo, valueInfoMan);
				}
				map.updateChild(key, mapElementValue);
			}
		}
		return map;
	}

	private static HAPStringableValueList processListValue(Object listObj, HAPValueInfoList listValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueList list = new HAPStringableValueList();
		
		if(listObj!=null){
			JSONArray listJsonObj = null;
			if(listObj instanceof JSONObject)  listJsonObj = ((JSONObject) listObj).optJSONArray(HAPAttributeConstant.ATTR_STRINGABLEVALUE_ELEMENTS);
			else if(listObj instanceof JSONArray)  listJsonObj = (JSONArray)listObj;
			
			HAPValueInfo childInfo = listValueInfo.getChildValueInfo().getSolidValueInfo();
			String childCategary = childInfo.getCategary();

			for(int i=0; i<listJsonObj.length(); i++){
				Object listEleObj = listJsonObj.opt(i);
				HAPStringableValue listElementValue = null;
				if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
					listElementValue = processBasicValue(listEleObj, (HAPValueInfoBasic)childInfo, valueInfoMan);
				}
				else{
					listElementValue = processComplexValue(listEleObj, childInfo, valueInfoMan);
				}
				if(listElementValue!=null)  list.addChild(listElementValue); 
			}
		}
		return list;
	}
	
	private static HAPStringableValue processBasicValue(Object basicValue, HAPValueInfoBasic basicValueInfo, HAPValueInfoManager valueInfoMan){
		String strValue = null;
		if(basicValue instanceof JSONObject)  strValue = ((JSONObject)basicValue).optString(HAPAttributeConstant.ATTR_STRINGABLEVALUE_VALUE);  
		else  strValue = basicValue.toString();
		
		HAPStringableValueBasic out = null;
		String type = basicValueInfo.getValueDataType();
		String defaultValue = basicValueInfo.getBasicAncestorValueString(HAPValueInfoBasic.ENTITY_PROPERTY_DEFAULTVALUE);
		out = new HAPStringableValueBasic(strValue, type, defaultValue);
		if(out.isEmpty())  out = null;
		return out;
	}

	private static HAPStringableValue processEntityOptionsValue(Object optionsValue, String keyValue, HAPValueInfoEntityOptions entityOptionsValueInfo, HAPValueInfoManager valueInfoMan){
		HAPValueInfo optionValueInfo = entityOptionsValueInfo.getOptionsValueInfo(keyValue).getSolidValueInfo();
		String categary = optionValueInfo.getCategary();
		HAPStringableValue out = null;
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(categary)){
			out = processBasicValue(optionsValue, (HAPValueInfoBasic)optionValueInfo, valueInfoMan);
		}
		else{
			out = processComplexValue(optionsValue, optionValueInfo, valueInfoMan);
		}
		return out;
	}
	
	private static HAPStringableValue processComplexValue(Object complexValue, HAPValueInfo valueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueComplex out = null;
		String categary = valueInfo.getCategary();
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			out = processEntityValue((JSONObject)complexValue, (HAPValueInfoEntity)valueInfo, valueInfoMan);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(categary)){
			out = processListValue(complexValue, (HAPValueInfoList)valueInfo, valueInfoMan);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(categary)){
			out = processMapValue(complexValue, (HAPValueInfoMap)valueInfo, valueInfoMan);
		}
		return out;
	}
	
	private static String getJsonFormat(JSONObject jsonObj){
		if(jsonObj.opt(HAPAttributeConstant.ATTR_STRINGABLEVALUE_CATEGARY)!=null)	return HAPConstant.CONS_SERIALIZATION_JSON_FULL;
		return HAPConstant.CONS_SERIALIZATION_JSON;
	}
}
