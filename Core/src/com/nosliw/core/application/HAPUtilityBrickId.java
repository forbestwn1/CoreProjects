package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.data.core.resource.HAPIdResourceType;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityBrickId {

	public static HAPIdBrick parseBrickIdAgressive(Object obj, HAPIdBrickType brickTypeIfNotProvided, String defaultDivision, HAPManagerApplicationBrick brickMan) {
		HAPIdBrick out = new HAPIdBrick();
		
		if(obj instanceof String) {
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		
		if(out.getBrickTypeId()==null) {
			out.setBrickTypeId(brickTypeIfNotProvided);
		}
		
		out.setBrickTypeId(normalizeBrickTypeId(out.getBrickTypeId(), brickMan));
		if(out.getDivision()==null) {
			out.setDivision(defaultDivision);
		}
		
		return out;
	}
	
	public static HAPIdBrickType parseBrickTypeIdAggresive(Object obj, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType brickTypeId = parseBrickTypeId(obj);
		return normalizeBrickTypeId(brickTypeId, brickMan);
	}
	
	public static HAPIdBrickType parseBrickTypeId(Object obj) {
		HAPIdBrickType out = null;
		if(obj instanceof String) {
			out = new HAPIdBrickType((String)obj);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdBrickType();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPIdBrickType normalizeBrickTypeId(HAPIdBrickType brickTypeId, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType out = brickTypeId;
		if(out.getVersion()==null) {
			out = brickMan.getLatestVersion(brickTypeId.getBrickType());
		}
		return out;
	}

	public static HAPIdBrickType parseBrickTypeId(Object entityTypeObj, HAPIdBrickType entityTypeIfNotProvided, HAPManagerApplicationBrick entityManager) {
		String entityType = null;
		String entityTypeVersion = null;
		if(entityTypeObj!=null) {
			HAPIdBrickType entityTypeId1 = parseBrickTypeId(entityTypeObj);
			entityType = entityTypeId1.getBrickType();
			entityTypeVersion = entityTypeId1.getVersion();
		}
		//try with entityTypeIfNotProvided
		if(entityTypeIfNotProvided!=null) {
			if(entityType==null) {
				entityType = entityTypeIfNotProvided.getBrickType();
			}
			if(entityTypeVersion==null) {
				entityTypeVersion = entityTypeIfNotProvided.getVersion();
			}
		}
		
		if(entityType==null) {
			return null;
		}
		
		//if version not provided, then use latest version
		if(entityTypeVersion==null) {
			entityTypeVersion = entityManager.getLatestVersion(entityType).getVersion();
		}
		return new HAPIdBrickType(entityType, entityTypeVersion);
	}
	

	public static HAPIdBrick parseBrickId(Object obj) {
		HAPIdBrick out = null;
		if(obj instanceof String) {
			out = new HAPIdBrick();
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdBrick();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		else if(obj instanceof HAPResourceIdSimple) {
			out = fromResourceId2BrickId((HAPResourceIdSimple)obj);
		}
		return out;
	}

	public static HAPIdBrick fromResourceId2BrickId(HAPResourceIdSimple resourceId) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(resourceId.getId());
		return new HAPIdBrick(new HAPIdBrickType(resourceId.getResourceTypeId().getResourceType(), resourceId.getResourceTypeId().getVersion()), segs.length>1?segs[1]:null, segs[0]);
	}
	
	public static HAPResourceIdSimple fromBrickId2ResourceId(HAPIdBrick brickId) {
		return new HAPResourceIdSimple(brickId.getBrickTypeId().getBrickType(), brickId.getBrickTypeId().getVersion(), HAPUtilityNamingConversion.cascadeElements(brickId.getId(), brickId.getDivision(), HAPConstantShared.SEPERATOR_LEVEL1));
	}

	public static HAPIdBrickType getBrickTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdBrickType(resourceId.getResourceTypeId().getResourceType(), resourceId.getResourceTypeId().getVersion());
	}
	
	public static HAPIdBrickType getBrickTypeIdFromResourceTypeId(HAPIdResourceType resourceTypeId) {
		return new HAPIdBrickType(resourceTypeId.getResourceType(), resourceTypeId.getVersion());
	}
	
	public static HAPIdResourceType getResourceTypeIdFromBrickTypeId(HAPIdBrickType brickTypeId) {
		return HAPFactoryResourceTypeId.newInstance(brickTypeId.getBrickType(), brickTypeId.getVersion());
	}
}
