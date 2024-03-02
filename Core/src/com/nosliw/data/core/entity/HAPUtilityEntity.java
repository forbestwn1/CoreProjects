package com.nosliw.data.core.entity;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
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

	public static HAPIdEntity createEntityId(Object obj) {
		HAPIdEntity out = null;
		if(obj instanceof String) {
			out = new HAPIdEntity();
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdEntity();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		else if(obj instanceof HAPResourceIdSimple) {
			out = fromResourceId2EntityId((HAPResourceIdSimple)obj);
		}
		return out;
	}

	public static HAPIdEntity fromResourceId2EntityId(HAPResourceIdSimple resourceId) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(resourceId.getId());
		return new HAPIdEntity(new HAPIdEntityType(resourceId.getResourceType(), resourceId.getVersion()), segs.length>1?segs[1]:null, segs[0]);
	}
	
	public static HAPResourceIdSimple fromEntityId2ResourceId(HAPIdEntity entityId) {
		return new HAPResourceIdSimple(entityId.getEntityTypeId().getEntityType(), HAPUtilityNamingConversion.cascadeLevel1(entityId.getId(), entityId.getDivision()), entityId.getEntityTypeId().getVersion());
	}

}
