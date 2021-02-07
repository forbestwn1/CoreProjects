package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPUtilityEntityInfo {

	public static HAPEntityInfo buildEntityInfoFromJson(JSONObject jsonObj) {
		HAPEntityInfo out = new HAPEntityInfoImp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	public static void cloneTo(HAPEntityInfo from, HAPEntityInfo to) {
		to.setId(from.getId());
		to.setName(from.getName());
		to.setStatus(from.getStatus());
		to.setDisplayName(from.getDisplayName());
		to.setDescription(from.getDescription());
		to.setInfo(from.getInfo().cloneInfo());
	}
	
	public static void buildJsonMap(Map<String, String> jsonMap, HAPEntityInfo entityInfo){
		jsonMap.put(HAPEntityInfo.ID, entityInfo.getId());
		jsonMap.put(HAPEntityInfo.NAME, entityInfo.getName());
		jsonMap.put(HAPEntityInfo.STATUS, entityInfo.getStatus());
		jsonMap.put(HAPEntityInfo.DISPLAYNAME, entityInfo.getDisplayName());
		jsonMap.put(HAPEntityInfo.DESCRIPTION, entityInfo.getDescription());
		jsonMap.put(HAPEntityInfo.INFO, HAPJsonUtility.buildJson(entityInfo.getInfo(), HAPSerializationFormat.JSON));
	}

	public static boolean isEnabled(JSONObject entityInfoJsonObj) {
		HAPEntityInfoImp entityInfo = new HAPEntityInfoImp();
		entityInfo.buildObject(entityInfoJsonObj, HAPSerializationFormat.JSON);
		return isEnabled(entityInfo);
	}
	
	public static boolean isEnabled(HAPEntityInfo entityInfo) {
		return !HAPConstant.ENTITYINFO_STATUS_DISABLED.equals(entityInfo.getStatus());
	}
	
	public static void processEntityId(HAPEntityInfo entityInfo) {
		String id = entityInfo.getId();
		if(HAPBasicUtility.isStringEmpty(id)) id = HAPConstant.NAME_DEFAULT;
		entityInfo.setId(id);
	}

}
