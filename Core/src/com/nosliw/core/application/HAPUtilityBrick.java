package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.executable.HAPAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.executable.HAPUtilityExport;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBrick {

	public static HAPBundle getBrickBundle(HAPResourceIdSimple resourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = brickMan.getBrickBundle(HAPUtilityBrickId.fromResourceId2BrickId(resourceId));
		HAPUtilityExport.exportBundle(resourceId, bundle);
		return bundle;
	}

	public static HAPBrick getBrickByResource(HAPInfoResourceIdNormalize normalizedResourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = getBrickBundle(normalizedResourceId.getRootResourceId(), brickMan);
		return getDescdentBrickLocal(bundle.getBrickWrapper(), normalizedResourceId.getPath());
	}
	
	public static HAPAttributeInBrick getDescendantAttribute(HAPBrick brick, HAPPath path) {
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
				if(i<path.getLength()-1) {
					throw new RuntimeException();
				}
			}
		}
		return attr;
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
	
	private static HAPResultBrick getDescdentBrickResult(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		return getDescendantBrickResult(rootBrickWrapper.getBrick(), path);
	}
	
	private static HAPResultBrick getDescendantBrickResult(HAPBrick brick, HAPPath path) {
		if(path==null||path.isEmpty()) {
			return new HAPResultBrick(brick);
		} else {
			HAPWrapperValue attrValueWrapper = getDescendantAttribute(brick, path).getValueWrapper();
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
	
}
