package com.nosliw.data.core.entity;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityEntity {

	public static HAPIdEntityType parseEntityTypeId(Object obj) {
		HAPIdEntityType out = null;
		if(obj instanceof String) {
			out = new HAPIdEntityType((String)obj);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdEntityType();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		return out;
	}

	public static HAPIdEntity createEntityId(HAPResourceIdSimple resourceId) {
		
	}
	
}
