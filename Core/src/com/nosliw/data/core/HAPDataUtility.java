package com.nosliw.data.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;

public class HAPDataUtility {

	public static Map<String, HAPDefinitionConstant> buildConstantDefinition(HAPAttachmentContainer attContainer){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		Map<String, HAPAttachment> attrs = attContainer.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_DATA);
		for(String name : attrs.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant();
			HAPAttachmentEntity attr = (HAPAttachmentEntity)attrs.get(name);
			constantDef.buildEntityInfoByJson(attr);
			constantDef.setData(buildDataWrapperFromObject(attr.getEntity()));
			out.put(constantDef.getName(), constantDef);
		}
		return out;
	}
	
	public static HAPDataWrapper buildDataWrapperFromObject(Object obj){
		HAPDataWrapper out = null;
		if(obj instanceof String){
			out = buildDataWrapper((String)obj);
		}
		else if(obj instanceof JSONObject){
			out = buildDataWrapperFromJson((JSONObject)obj);
		}
		return out;
	}
	
	
	public static HAPDataWrapper buildDataWrapper(String strValue){
		HAPDataWrapper wrapper = new HAPDataWrapper();
		if(wrapper.buildObjectByLiterate(strValue))  return wrapper;
		return null;
	}

	public static HAPDataWrapper buildDataWrapperFromJson(JSONObject jsonObj){
		if(jsonObj==null)   return null;
		HAPDataWrapper wrapper = new HAPDataWrapper();
		boolean result = wrapper.buildObjectByJson(jsonObj);
		if(result)   return wrapper;
		else return null;
	}

	public static Map<String, HAPData> buildDataWrapperMap(Object obj){
		JSONObject jsonObj = null;
		if(obj instanceof String) {
			if(HAPBasicUtility.isStringNotEmpty((String)obj)) {
				jsonObj = new JSONObject((String)obj);
			}
		}
		else if(obj instanceof JSONObject){
			jsonObj = (JSONObject)obj;
		}
		return buildDataWrapperMapFromJson(jsonObj);
	}
	
	public static Map<String, HAPData> buildDataWrapperMapFromJson(JSONObject jsonObj){
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>();
		if(jsonObj!=null) {
			Iterator<String> it2 = jsonObj.keys();
			while(it2.hasNext()){
				String constantName = it2.next();
				JSONObject constantJson = jsonObj.getJSONObject(constantName);
				constants.put(constantName, HAPDataUtility.buildDataWrapperFromJson(constantJson));
			}
		}
		return constants;
	}
	
	public static boolean isNormalDataOpration(HAPOperation operation){
		String type = operation.getType();
		return type==null || HAPConstant.DATAOPERATION_TYPE_NORMAL.equals(type);
	}
	
}
