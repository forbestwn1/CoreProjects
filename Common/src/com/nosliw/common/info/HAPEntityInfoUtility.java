package com.nosliw.common.info;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

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

}
