package com.nosliw.core.application;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;

public class HAPUtilityBrick {

	public static HAPBrick getBrickByResource(HAPInfoResourceIdNormalize normalizedResourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = HAPUtilityBundle.getBrickBundle(normalizedResourceId.getRootResourceId(), brickMan);
		return getDescdentBrickLocal(bundle, normalizedResourceId.getPath(), HAPConstantShared.NAME_ROOTBRICK_MAIN);
	}
	
	public static HAPAttributeInBrick getAttributeInBrick(HAPBrick brick, String attrName) {
		HAPAttributeInBrick out = null;
		for(HAPAttributeInBrick attr : brick.getAttributes()) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return out;
	}

	
	public static HAPBrick getBrick(HAPEntityOrReference brickOrRef, HAPManagerApplicationBrick brickManager) {
		HAPBrick out = null;
		String type = brickOrRef.getEntityOrReferenceType();
		if(type.equals(HAPConstantShared.BRICK)) {
			out = (HAPBrick)brickOrRef;
		}
		else if(type.equals(HAPConstantShared.RESOURCEID)) {
			out = HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId((HAPResourceId)brickOrRef), brickManager);			
		}
		return out;
	}
	
	public static HAPWrapperBrickRoot getBrickRoot(String rootBrickName, HAPBundle bundle) {
		return bundle.getRootBrickWrapper(rootBrickName);
	}

	public static HAPResultBrickDescentValue getDescdentBrickResult(HAPBundle bundle, HAPPath path, String rootNameIfNotProvide) {
		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path.toString(), rootNameIfNotProvide);
		return getDescendantResult(bundle.getRootBrickWrapper(fullPathInfo.getRoot()).getBrick(), fullPathInfo.getPath());
	}

	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPPath path, String rootNameIfNotProvide) {
		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path.toString(), rootNameIfNotProvide);
		return getDescdentBrickLocal(bundle.getRootBrickWrapper(fullPathInfo.getRoot()).getBrick(), fullPathInfo.getPath());
	}

	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPPath path) {
		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path);
		return getDescdentBrickLocal(bundle.getRootBrickWrapper(fullPathInfo.getRoot()).getBrick(), fullPathInfo.getPath());
	}

	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPIdBrickInBundle brickInBundleId, String rootNameIfNotProvide) {
		return getDescdentBrickLocal(bundle, new HAPPath(brickInBundleId.getIdPath()), rootNameIfNotProvide);
	}

	public static HAPWrapperValueOfReferenceResource getDescdentResourceResourceIdLocal(HAPBundle bundle, HAPPath path) {
		HAPAttributeInBrick attr = getDescendantAttribute(bundle, path);
		return (HAPWrapperValueOfReferenceResource)attr.getValueWrapper();
	}
	
	public static HAPAttributeInBrick getDescendantAttribute(HAPBundle bundle, HAPPath path) {
		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path);
		return getDescendantAttribute(bundle.getRootBrickWrapper(fullPathInfo.getRoot()).getBrick(), fullPathInfo.getPath());
	}
	
	
	private static HAPBrick getDescdentBrickLocal(HAPBrick brick, HAPPath path) {
		HAPResultBrickDescentValue brickResult = getDescendantResult(brick, path);
		if(brickResult!=null) {
			return brickResult.getBrick();
		}
		return null;
	}
	
	
	

	private static HAPResultBrickDescentValue getDescendantResult(HAPBrick brick, HAPPath path) {
		if(path==null||path.isEmpty()) {
			return new HAPResultBrickDescentValue(brick);
		} else {
			HAPWrapperValue attrValueWrapper = getDescendantAttribute(brick, path).getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				return new HAPResultBrickDescentValue(((HAPWithBrick)attrValueWrapper).getBrick());
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				HAPWrapperValueOfReferenceResource valueWrapper = (HAPWrapperValueOfReferenceResource)attrValueWrapper;
				return new HAPResultBrickDescentValue(valueWrapper.getResourceId());
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_DYNAMIC)) {
				HAPWrapperValueOfDynamic valueWrapper = (HAPWrapperValueOfDynamic)attrValueWrapper;
				return new HAPResultBrickDescentValue(valueWrapper.getDynamicValue());
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE)) {
				HAPWrapperValueOfValue valueWrapper = (HAPWrapperValueOfValue)attrValueWrapper;
				return new HAPResultBrickDescentValue(valueWrapper.getValue());
			}
			return null;
		}
	}

	private static HAPAttributeInBrick getDescendantAttribute(HAPBrick brick, HAPPath path) {
		if(path==null||path.isEmpty()) {
			throw new RuntimeException();
		}
		
		HAPAttributeInBrick attr = null;
		HAPBrick currentBrick = brick;
		for(int i=0; i<path.getLength(); i++) {
			String attrName = path.getPathSegments()[i];
			attr = getAttributeInBrick(currentBrick, attrName);
			HAPWrapperValue attrValueWrapper = attr.getValueWrapper();
			String attrValueType = attrValueWrapper.getValueType();
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				currentBrick = ((HAPWrapperValueOfBrick)attrValueWrapper).getBrick();
			}
			else {
				if(i<path.getLength()-1) {
					throw new RuntimeException();
				}
			}
		}
		return attr;
	}
	
}
