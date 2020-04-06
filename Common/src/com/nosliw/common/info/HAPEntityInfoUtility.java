package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPEntityInfoUtility {

	public static void cloneTo(HAPEntityInfo from, HAPEntityInfoWritable to) {
		to.setInfo(from.getInfo().cloneInfo());
		to.setName(from.getName());
		to.setDescription(from.getDescription());
	}
	
	public static void buildJsonMap(Map<String, String> jsonMap, HAPEntityInfo entityInfo){
		jsonMap.put(HAPEntityInfo.NAME, entityInfo.getName());
		jsonMap.put(HAPEntityInfo.DESCRIPTION, entityInfo.getDescription());
		jsonMap.put(HAPEntityInfo.INFO, HAPJsonUtility.buildJson(entityInfo.getInfo(), HAPSerializationFormat.JSON));
	}

	public static boolean isEnabled(JSONObject entityInfoJsonObj) {
		HAPEntityInfoImp entityInfo = new HAPEntityInfoImp();
		entityInfo.buildObject(entityInfoJsonObj, HAPSerializationFormat.JSON);
		return isEnabled(entityInfo);
	}
	
	public static boolean isEnabled(HAPEntityInfo entityInfo) {
		String value = (String)entityInfo.getInfo().getValue(HAPConstant.ENTITYINFO_INFONAME_DISABLE);
		if(value!=null)   return false;
		else  return true;
	}
	
}
