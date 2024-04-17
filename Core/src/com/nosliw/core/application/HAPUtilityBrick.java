package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityBrick {

	public static HAPBrick getBrickByResource(HAPInfoResourceIdNormalize normalizedResourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = brickMan.getBrickBundle(normalizedResourceId.getRootResourceIdSimple());
		return getDescdentBrick(bundle.getBrickWrapper(), normalizedResourceId.getPath(), brickMan);
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
			HAPWrapperValueInAttribute attrValueWrapper = attr.getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				currentBrick = ((HAPWrapperValueInAttributeBrick)attrValueWrapper).getBrick();
			}
			else {
				HAPPath remainPath = path.getRemainingPath(i+1);
				if(!remainPath.isEmpty()) {
					if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
						return new HAPResultAttribute(attr, remainPath);
					}
					else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE)) {
						throw new RuntimeException();
					}
				}
			}
		}
		return new HAPResultAttribute(attr, new HAPPath());
	}

	public static HAPBrick getDescdentBrick(HAPWrapperBrick rootBrickWrapper, HAPPath path, HAPManagerApplicationBrick brickMan) {
		HAPResultBrick brickResult = getDescdentBrickResult(rootBrickWrapper, path, brickMan);
		if(brickResult!=null) {
			return brickResult.getBrick();
		}
		return null;
	}
	
	public static HAPResultBrick getDescdentBrickResult(HAPWrapperBrick rootBrickWrapper, HAPPath path, HAPManagerApplicationBrick brickMan) {
		return getDescendantBrickResult(rootBrickWrapper.getBrick(), path, brickMan);
	}
	
	private static HAPResultBrick getDescendantBrickResult(HAPBrick brick, HAPPath path, HAPManagerApplicationBrick brickMan) {
		if(path==null||path.isEmpty()) {
			return new HAPResultBrick(brick);
		} else {
			HAPResultAttribute attrResult = getDescendantAttributeResult(brick, path);
			HAPWrapperValueInAttribute attrValueWrapper = attrResult.getAttribute().getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				return new HAPResultBrick(((HAPWithBrick)attrValueWrapper).getBrick());
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE)) {
				throw new RuntimeException();
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				HAPWrapperValueInAttributeReferenceResource valueWrapper = (HAPWrapperValueInAttributeReferenceResource)attrValueWrapper;
				HAPBundle attrBundle = brickMan.getBrickBundle(valueWrapper.getNormalizedResourceId().getRootResourceIdSimple());
				HAPPath attrPath = new HAPPath(valueWrapper.getNormalizedResourceId().getPath()).appendPath(attrResult.getRemainPath());
				HAPResultBrick refBrickResult = getDescendantBrickResult(attrBundle.getBrickWrapper().getBrick(), attrPath, brickMan);
				if(refBrickResult.isInternalBrick()) {
					return new HAPResultBrick(new HAPReferenceBrickGlobal(attrBundle, attrPath));
				} else {
					return refBrickResult;
				}
			}
		}
		return null;
	}
	
	
	public static HAPBrick getBrick(HAPReferenceBrickLocal brickRef, String baseBrickPath, HAPBundle bundle) {
		HAPBrick brick = null;
		if(brickRef.getIdPath()!=null) {
			brick = bundle.getBrickByPath(new HAPPath(brickRef.getIdPath()));
		}
		else if(brickRef.getRelativePath()!=null) {
			
		}
		return brick;
	}
	
	public static HAPTreeNode getDescdentTreeNode(HAPWrapperBrick rootBrickWrapper, HAPPath path) {
		HAPTreeNode out = null;
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
		return new HAPIdBrick(new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion()), segs.length>1?segs[1]:null, segs[0]);
	}
	
	public static HAPResourceIdSimple fromBrickId2ResourceId(HAPIdBrick brickId) {
		return new HAPResourceIdSimple(brickId.getBrickTypeId().getBrickType(), brickId.getBrickTypeId().getVersion(), HAPUtilityNamingConversion.cascadeLevel1(brickId.getId(), brickId.getDivision()));
	}

	public static HAPIdBrickType getBrickTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion());
	}
	
}
