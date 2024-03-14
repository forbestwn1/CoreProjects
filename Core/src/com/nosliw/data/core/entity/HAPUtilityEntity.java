package com.nosliw.data.core.entity;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityEntity {

	public static HAPTreeNode getDescdentTreeNode(HAPInfoEntity rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getEntity(), path);
		}
		return out;
	}
	
	public static HAPEntityExecutable getDescdentEntity(HAPInfoEntity rootEntityInfo, HAPPath path) {
		HAPEntityExecutable out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo.getEntity();
		}
		else {
			out = getDescendantEntity(rootEntityInfo.getEntity(), path); 
		}
		return out;
	}
	
	public static HAPAttributeExecutable getDescendantAttribute(HAPEntityExecutable entityExe, HAPPath path) {
		HAPAttributeExecutable out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = entityExe.getAttribute(attribute);
			} else {
				HAPInfoAttributeValue attrValueInfo = out.getValueInfo();
				if(attrValueInfo instanceof HAPWithEntity) {
					out = ((HAPWithEntity)attrValueInfo).getEntity().getAttribute(attribute);
				}
				else{
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	private static HAPEntityExecutable getDescendantEntity(HAPEntityExecutable entityExe, HAPPath path) {
		HAPEntityExecutable out = null;
		if(path==null||path.isEmpty()) {
			out = entityExe;
		} else {
			HAPInfoAttributeValue attrValueInfo = getDescendantAttribute(entityExe, path).getValueInfo();
			if(attrValueInfo instanceof HAPWithEntity) {
				out = ((HAPWithEntity)attrValueInfo).getEntity();
			}
		}
		return out;
	}
	
	
	
	public static boolean isEntityComplex(HAPIdEntityType entityTypeId, HAPManagerEntity entityMan) {
		return entityMan.getEntityTypeInfo(entityTypeId).getIsComplex();
	}
	
	public static HAPIdEntity parseEntityIdAgressive(Object obj, String defaultDivision, HAPManagerEntity entityMan) {
		HAPIdEntity out = new HAPIdEntity();
		
		if(obj instanceof String) {
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		
		out.setEntityTypeId(normalizeEntityTypeId(out.getEntityTypeId(), entityMan));
		if(out.getDivision()==null) {
			out.setDivision(defaultDivision);
		}
		
		return out;
	}
	
	public static HAPIdEntityType parseEntityTypeIdAggresive(Object obj, HAPManagerEntity entityMan) {
		HAPIdEntityType entityTypeId = parseEntityTypeId(obj);
		return normalizeEntityTypeId(entityTypeId, entityMan);
	}
	
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
	
	public static HAPIdEntityType normalizeEntityTypeId(HAPIdEntityType entityTypeId, HAPManagerEntity entityMan) {
		HAPIdEntityType out = entityTypeId;
		if(out.getVersion()==null) {
			out = entityMan.getLatestVersion(entityTypeId.getEntityType());
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

	public static HAPIdEntityType getEntityTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdEntityType(resourceId.getResourceType(), resourceId.getVersion());
	}
	
}
