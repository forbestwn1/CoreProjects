package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPUtilityEntityInfo {

	public static void softMerge(HAPEntityInfo target, HAPEntityInfo source) {
		if(HAPUtilityBasic.isStringEmpty(target.getId()))  target.setId(source.getId());
		if(HAPUtilityBasic.isStringEmpty(target.getName()))  target.setName(source.getName());
		if(HAPUtilityBasic.isStringEmpty(target.getDescription()))  target.setDescription(source.getDescription());
		if(HAPUtilityBasic.isStringEmpty(target.getDisplayName()))  target.setDisplayName(source.getDisplayName());
		HAPUtilityInfo.softMerge(target.getInfo(), source.getInfo());
		enable(target, isEnabled(target) && isEnabled(source));
	}
	
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
		jsonMap.put(HAPEntityInfo.INFO, HAPUtilityJson.buildJson(entityInfo.getInfo(), HAPSerializationFormat.JSON));
	}

	public static boolean isEnabled(JSONObject entityInfoJsonObj) {
		HAPEntityInfoImp entityInfo = new HAPEntityInfoImp();
		entityInfo.buildObject(entityInfoJsonObj, HAPSerializationFormat.JSON);
		return isEnabled(entityInfo);
	}
	
	public static boolean isEnabled(HAPEntityInfo entityInfo) {
		return !HAPConstantShared.ENTITYINFO_STATUS_DISABLED.equals(entityInfo.getStatus());
	}

	public static void enable(HAPEntityInfo entityInfo, boolean enable) {
		if(enable)  entityInfo.setStatus(HAPConstantShared.ENTITYINFO_STATUS_ENABLED);
		else entityInfo.setStatus(HAPConstantShared.ENTITYINFO_STATUS_DISABLED);
	}

	public static void processEntityId(HAPEntityInfo entityInfo) {
		String id = entityInfo.getId();
		if(HAPUtilityBasic.isStringEmpty(id)) id = HAPConstantShared.NAME_DEFAULT;
		entityInfo.setId(id);
	}

}
