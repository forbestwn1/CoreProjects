package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityEntity {

	public static HAPTreeNode getDescdentTreeNode(HAPWrapperBrick rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getBrick(), path);
		}
		return out;
	}
	
	public static HAPBrick getDescdentEntity(HAPWrapperBrick rootEntityInfo, HAPPath path) {
		HAPBrick out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo.getBrick();
		}
		else {
			out = getDescendantEntity(rootEntityInfo.getBrick(), path); 
		}
		return out;
	}
	
	public static HAPAttributeInBrick getDescendantAttribute(HAPBrick entityExe, HAPPath path) {
		HAPAttributeInBrick out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = entityExe.getAttribute(attribute);
			} else {
				HAPWrapperValueInAttribute attrValueInfo = out.getValueWrapper();
				if(attrValueInfo instanceof HAPWithBrick) {
					out = ((HAPWithBrick)attrValueInfo).getBrick().getAttribute(attribute);
				}
				else{
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	private static HAPBrick getDescendantEntity(HAPBrick entityExe, HAPPath path) {
		HAPBrick out = null;
		if(path==null||path.isEmpty()) {
			out = entityExe;
		} else {
			HAPWrapperValueInAttribute attrValueInfo = getDescendantAttribute(entityExe, path).getValueWrapper();
			if(attrValueInfo instanceof HAPWithBrick) {
				out = ((HAPWithBrick)attrValueInfo).getBrick();
			}
		}
		return out;
	}
	
	
	
	public static boolean isEntityComplex(HAPIdBrickType entityTypeId, HAPManagerApplicationBrick entityMan) {
		return entityMan.getEntityTypeInfo(entityTypeId).getIsComplex();
	}
	
	public static HAPIdBrick parseEntityIdAgressive(Object obj, String defaultDivision, HAPManagerApplicationBrick entityMan) {
		HAPIdBrick out = new HAPIdBrick();
		
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
	
	public static HAPIdBrickType parseEntityTypeIdAggresive(Object obj, HAPManagerApplicationBrick entityMan) {
		HAPIdBrickType entityTypeId = parseEntityTypeId(obj);
		return normalizeEntityTypeId(entityTypeId, entityMan);
	}
	
	public static HAPIdBrickType parseEntityTypeId(Object obj) {
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
	
	public static HAPIdBrickType normalizeEntityTypeId(HAPIdBrickType entityTypeId, HAPManagerApplicationBrick entityMan) {
		HAPIdBrickType out = entityTypeId;
		if(out.getVersion()==null) {
			out = entityMan.getLatestVersion(entityTypeId.getEntityType());
		}
		return out;
		
	}

	public static HAPIdBrick createEntityId(Object obj) {
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
			out = fromResourceId2EntityId((HAPResourceIdSimple)obj);
		}
		return out;
	}

	public static HAPIdBrick fromResourceId2EntityId(HAPResourceIdSimple resourceId) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(resourceId.getId());
		return new HAPIdBrick(new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion()), segs.length>1?segs[1]:null, segs[0]);
	}
	
	public static HAPResourceIdSimple fromEntityId2ResourceId(HAPIdBrick entityId) {
		return new HAPResourceIdSimple(entityId.getEntityTypeId().getEntityType(), HAPUtilityNamingConversion.cascadeLevel1(entityId.getId(), entityId.getDivision()), entityId.getEntityTypeId().getVersion());
	}

	public static HAPIdBrickType getEntityTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion());
	}
	
}
