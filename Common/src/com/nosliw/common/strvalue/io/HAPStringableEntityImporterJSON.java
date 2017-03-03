package com.nosliw.common.strvalue.io;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.strvalue.HAPStringableValueComplex;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoAtomic;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntityOptions;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoList;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoMap;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityImporterJSON {
	
	public static <T> T parseJsonEntity(JSONObject jsonObjEntity, Class<T> entityClass, HAPValueInfoManager valueInfoMan){
		HAPValueInfoEntity entityValueInfo = valueInfoMan.getEntityValueInfoByClass(entityClass); 
		HAPStringableValueEntity entity = (HAPStringableValueEntity)entityValueInfo.newValue();
		buildStringableValueEntity(jsonObjEntity, entity, entityValueInfo, valueInfoMan);
		return (T)entity;
	}

	public static HAPStringableValueEntity parseJsonEntity(InputStream inputStream, String entityType, HAPValueInfoManager valueInfoMan){
		HAPStringableValueEntity out = null;		
		try {
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject jsonObjEntity = new JSONObject(content);
			 out = parseJsonEntity(jsonObjEntity, entityType, valueInfoMan);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static HAPStringableValueEntity parseJsonEntity(JSONObject jsonObjEntity, String entityType, HAPValueInfoManager valueInfoMan){
		HAPValueInfoEntity entityValueInfo = (HAPValueInfoEntity)valueInfoMan.getValueInfo(entityType);
		HAPStringableValueEntity entity = parseJsonEntity(jsonObjEntity, entityValueInfo, valueInfoMan);
		return entity;
	}

	public static HAPStringableValueEntity parseJsonEntity(JSONObject jsonObjEntity, HAPValueInfoEntity entityValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueEntity entity = (HAPStringableValueEntity)entityValueInfo.newValue();
		buildStringableValueEntity(jsonObjEntity, entity, entityValueInfo, valueInfoMan);
		return entity;
	}

	
	
	
	
	public static void buildStringableValueEntity(JSONObject jsonObjEntity, HAPStringableValueEntity entity, Class entityClass, HAPValueInfoManager valueInfoMan){
		HAPValueInfoEntity entityValueInfo = valueInfoMan.getEntityValueInfoByClass(entityClass); 
		buildStringableValueEntity(jsonObjEntity, entity, entityValueInfo, valueInfoMan);
	}
	
	public static void buildStringableValueEntity(JSONObject jsonObjEntity, HAPStringableValueEntity entity, HAPValueInfoEntity entityValueInfo, HAPValueInfoManager valueInfoMan){
		if(jsonObjEntity!=null){
			JSONObject propertiesJsonObj = jsonObjEntity;
			HAPSerializationFormat jsonFormat = getJsonFormat(jsonObjEntity);
			if(HAPSerializationFormat.JSON_FULL.equals(jsonFormat))  propertiesJsonObj = jsonObjEntity.optJSONObject(HAPStringableValueEntity.PROPERTIES);

			Set<String> entityOptionsProperties = new HashSet<String>();
			
			for(String property : entityValueInfo.getEntityProperties()){
				
				if("constants".equals(property)){
					int kkkk = 5555;
					kkkk++;
				}
				
				
				HAPValueInfo propertyValueInfo = entityValueInfo.getPropertyInfo(property);
				String propertyCategary = propertyValueInfo.getValueInfoType();
				if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary))  entityOptionsProperties.add(property);
				else{
					HAPStringableValue entityProperty = readPropertyValueOfEntity(propertiesJsonObj, propertyValueInfo, valueInfoMan);
					if(entityProperty!=null)			entity.updateChild(property, entityProperty);
				}
			}
			
			for(String propertyName : entityOptionsProperties){
				HAPValueInfoEntityOptions propertyValueInfo = (HAPValueInfoEntityOptions)entityValueInfo.getPropertyInfo(propertyName).getSolidValueInfo();
				Object propertyObj = propertiesJsonObj.opt(propertyName);
				String keyValue = entity.getAtomicAncestorValueString(propertyValueInfo.getKeyName());
				HAPStringableValue entityProperty = processEntityOptionsValue(propertyObj, keyValue, propertyValueInfo, valueInfoMan);
				if(entityProperty!=null)			entity.updateChild(propertyName, entityProperty);
			}
		}
		HAPValueInfoUtility.validateStringableValueEntity(entityValueInfo, entity);
	}
	
	private static HAPStringableValue readPropertyValueOfEntity(JSONObject propertiesJsonObj, HAPValueInfo propertyValueInfo, HAPValueInfoManager valueInfoMan){
		String propertyName = propertyValueInfo.getName();
		
		HAPStringableValue out = null;
		try{
			HAPValueInfo propertyInfo = propertyValueInfo.getSolidValueInfo();
			String propertyCategary = propertyInfo.getValueInfoType();
			Object propertyObj = propertiesJsonObj.opt(propertyName);
			if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(propertyCategary)){
				out = parseJsonList(propertyObj, (HAPValueInfoList)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(propertyCategary)){
				out = parseJsonMap((JSONObject)propertyObj, (HAPValueInfoMap)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(propertyCategary)){
				out = parseJsonEntity((JSONObject)propertyObj, (HAPValueInfoEntity)propertyInfo, valueInfoMan);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary)){
				throw new Exception();
			}
			else{
				out = processAtomicValue(propertyObj, (HAPValueInfoAtomic)propertyInfo, valueInfoMan);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public static HAPStringableValueMap parseJsonMap(JSONObject mapObj, HAPValueInfoMap mapValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueMap map = (HAPStringableValueMap)mapValueInfo.newValue();
		
		try{
			if(mapObj!=null){
				JSONObject mapPropertiesJsonObj = null;
				HAPSerializationFormat jsonFormat = getJsonFormat(mapObj);
				if(HAPSerializationFormat.JSON_FULL.equals(jsonFormat))  mapPropertiesJsonObj = (mapObj).optJSONObject(HAPStringableValueMap.ELEMENTS);
				else    mapPropertiesJsonObj = mapObj;
				
				HAPValueInfo childInfo = mapValueInfo.getChildValueInfo().getSolidValueInfo();
				String childCategary = childInfo.getValueInfoType();

				Iterator<String> it = mapPropertiesJsonObj.keys();
				while(it.hasNext()){
					String key = it.next();
					Object keyValue = mapPropertiesJsonObj.opt(key);
					HAPStringableValue mapElementValue = null;
					if(HAPConstant.STRINGALBE_VALUEINFO_ATOMIC.equals(childCategary)){
						mapElementValue = processAtomicValue(keyValue, (HAPValueInfoAtomic)childInfo, valueInfoMan);
					}		
					else{
						if(keyValue instanceof JSONObject){
							//for key value is JSONObject, add key as property, so that we don't need to duplicate the key in value part in original json
							String mapKey = mapValueInfo.getAtomicAncestorValueString(HAPValueInfoMap.KEY);
							((JSONObject)keyValue).putOpt(mapKey, key);
						}
						mapElementValue = processComplexValue(keyValue, childInfo, valueInfoMan);
					}
					map.updateChild(key, mapElementValue);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}

	public static HAPStringableValueList parseJsonList(Object listObj, HAPValueInfoList listValueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueList list = (HAPStringableValueList)listValueInfo.newValue();
		
		if(listObj!=null){
			JSONArray listJsonObj = null;
			if(listObj instanceof JSONObject)  listJsonObj = ((JSONObject) listObj).optJSONArray(HAPStringableValueList.ELEMENTS);
			else if(listObj instanceof JSONArray)  listJsonObj = (JSONArray)listObj;
			
			HAPValueInfo childInfo = listValueInfo.getChildValueInfo().getSolidValueInfo();
			String childCategary = childInfo.getValueInfoType();

			for(int i=0; i<listJsonObj.length(); i++){
				Object listEleObj = listJsonObj.opt(i);
				HAPStringableValue listElementValue = null;
				if(HAPConstant.STRINGALBE_VALUEINFO_ATOMIC.equals(childCategary)){
					listElementValue = processAtomicValue(listEleObj, (HAPValueInfoAtomic)childInfo, valueInfoMan);
				}
				else{
					listElementValue = processComplexValue(listEleObj, childInfo, valueInfoMan);
				}
				if(listElementValue!=null)  list.addChild(listElementValue); 
			}
		}
		return list;
	}
	
	private static HAPStringableValue processAtomicValue(Object basicValue, HAPValueInfoAtomic atomicValueInfo, HAPValueInfoManager valueInfoMan){
		if(basicValue==null)  return null;
		
		String strValue = null;
		if(basicValue instanceof JSONObject){
			//check if atom is in full json
			Object v = ((JSONObject)basicValue).opt(HAPStringableValueAtomic.VALUE);
			if(v==null)   strValue = basicValue.toString();
			else   strValue = v.toString();
		}
		else  strValue = basicValue.toString();
		
		HAPStringableValueAtomic out = null;
		if(strValue!=null)  out = new HAPStringableValueAtomic(strValue, atomicValueInfo.getDataType(), atomicValueInfo.getSubDataType());

		return out;
	}

	private static HAPStringableValue processEntityOptionsValue(Object optionsValue, String keyValue, HAPValueInfoEntityOptions entityOptionsValueInfo, HAPValueInfoManager valueInfoMan){
		HAPValueInfo optionValueInfo = entityOptionsValueInfo.getOptionsValueInfo(keyValue).getSolidValueInfo();
		String categary = optionValueInfo.getValueInfoType();
		HAPStringableValue out = null;
		if(HAPConstant.STRINGALBE_VALUEINFO_ATOMIC.equals(categary)){
			out = processAtomicValue(optionsValue, (HAPValueInfoAtomic)optionValueInfo, valueInfoMan);
		}
		else{
			out = processComplexValue(optionsValue, optionValueInfo, valueInfoMan);
		}
		return out;
	}
	
	private static HAPStringableValue processComplexValue(Object complexValue, HAPValueInfo valueInfo, HAPValueInfoManager valueInfoMan){
		HAPStringableValueComplex out = null;
		String categary = valueInfo.getValueInfoType();
		if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			out = parseJsonEntity((JSONObject)complexValue, (HAPValueInfoEntity)valueInfo, valueInfoMan);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(categary)){
			out = parseJsonList(complexValue, (HAPValueInfoList)valueInfo, valueInfoMan);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(categary)){
			out = parseJsonMap((JSONObject)complexValue, (HAPValueInfoMap)valueInfo, valueInfoMan);
		}
		return out;
	}
	
	private static HAPSerializationFormat getJsonFormat(JSONObject jsonObj){
		if(jsonObj.opt(HAPStringableValue.STRUCTURE)!=null)	return HAPSerializationFormat.JSON_FULL;
		return HAPSerializationFormat.JSON;
	}
}
