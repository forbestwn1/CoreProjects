package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.data.core.resource.HAPIdResourceType;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBrick {

	public static HAPBrick getBrickByResource(HAPInfoResourceIdNormalize normalizedResourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = brickMan.getBrickBundle(normalizedResourceId.getRootResourceId());
		return getDescdentBrickLocal(bundle.getBrickWrapper(), normalizedResourceId.getPath());
	}
	
	public static HAPAttributeInBrick getDescendantAttribute(HAPBrick brick, HAPPath path) {
		return getDescendantAttributeResult(brick, path).getAttribute();
	}
	
	public static HAPResultAttribute getDescendantAttributeResult(HAPBrick brick, HAPPath path) {
		if(path==null||path.isEmpty()) {
			throw new RuntimeException();
		}
		
		HAPAttributeInBrick attr = null;
		HAPBrick currentBrick = brick;
		for(int i=0; i<path.getLength(); i++) {
			String attrName = path.getPathSegments()[i];
			attr = currentBrick.getAttribute(attrName);
			HAPWrapperValue attrValueWrapper = attr.getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				currentBrick = ((HAPWrapperValueOfBrick)attrValueWrapper).getBrick();
			}
			else {
				HAPPath remainPath = path.getRemainingPath(i+1);
				if(!remainPath.isEmpty()) {
					throw new RuntimeException();
				}
			}
		}
		return new HAPResultAttribute(attr);
	}

	public static HAPBrick getDescdentBrick(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick out = null;
		HAPResultBrick brickResult = getDescdentBrickResult(rootBrickWrapper, path);
		if(brickResult.isInternalBrick()) {
			out = brickResult.getInternalBrick();
		}
		else {
			HAPResourceDataBrick resourceData =(HAPResourceDataBrick)HAPUtilityResource.getResource(brickResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
			out = resourceData.getBrick();
		}
		return out;
	}
	
	public static HAPBrick getDescdentBrickLocal(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		HAPResultBrick brickResult = getDescdentBrickResult(rootBrickWrapper, path);
		if(brickResult!=null) {
			return brickResult.getInternalBrick();
		}
		return null;
	}
	
	public static HAPResultBrick getDescdentBrickResult(HAPBundle bundle, HAPPath path) {
		return getDescdentBrickResult(bundle.getBrickWrapper(), path);
	}
	
	public static HAPResultBrick getDescdentBrickResult(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		return getDescendantBrickResult(rootBrickWrapper.getBrick(), path);
	}
	
	private static HAPResultBrick getDescendantBrickResult(HAPBrick brick, HAPPath path) {
		if(path==null||path.isEmpty()) {
			return new HAPResultBrick(brick);
		} else {
			HAPResultAttribute attrResult = getDescendantAttributeResult(brick, path);
			HAPWrapperValue attrValueWrapper = attrResult.getAttribute().getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				return new HAPResultBrick(((HAPWithBrick)attrValueWrapper).getBrick());
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				HAPWrapperValueOfReferenceResource valueWrapper = (HAPWrapperValueOfReferenceResource)attrValueWrapper;
				return new HAPResultBrick(valueWrapper.getResourceId());
			}
			return null;
		}
	}
	
	public static HAPTreeNodeBrick getDescdentTreeNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		HAPTreeNodeBrick out = null;
		if(path==null || path.isEmpty()) {
			out = rootBrickWrapper;
		}
		else {
			out = getDescendantAttribute(rootBrickWrapper.getBrick(), path);
		}
		return out;
	}
	
	
	
	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManagerApplicationBrick brickAppMan) {
		return brickAppMan.getBrickTypeInfo(brickTypeId).getIsComplex();
	}
	
	public static HAPIdBrick parseBrickIdAgressive(Object obj, String defaultDivision, HAPManagerApplicationBrick brickMan) {
		HAPIdBrick out = new HAPIdBrick();
		
		if(obj instanceof String) {
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out.buildObject(obj, HAPSerializationFormat.JSON);
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
		return new HAPResourceIdSimple(brickId.getBrickTypeId().getBrickType(), brickId.getBrickTypeId().getVersion(), HAPUtilityNamingConversion.cascadeLevel1(brickId.getId(), brickId.getDivision()));
	}

	public static HAPIdBrickType getBrickTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdBrickType(resourceId.getResourceTypeId().getResourceType(), resourceId.getResourceTypeId().getVersion());
	}
	
	public static HAPIdBrickType getBrickTypeIdFromResourceTypeId(HAPIdResourceType resourceTypeId) {
		return new HAPIdBrickType(resourceTypeId.getResourceType(), resourceTypeId.getVersion());
	}
	
	public static HAPIdResourceType getResourceTypeIdFromBrickTypeId(HAPIdBrickType brickTypeId) {
		return new HAPIdResourceType(brickTypeId.getBrickType(), brickTypeId.getVersion());
	}
}
