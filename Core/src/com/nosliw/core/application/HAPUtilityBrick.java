package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.valueport.HAPInfoValuePortContainer;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBrick {

	public static HAPBundle getBrickBundle(HAPResourceIdSimple resourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = brickMan.getBrickBundle(HAPUtilityBrickId.fromResourceId2BrickId(resourceId));
		HAPUtilityExport.exportBundle(resourceId, bundle);
		return bundle;
	}

	public static HAPAttributeInBrick getDescendantAttribute(HAPBrick brick, HAPPath path) {
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
	
	public static HAPAttributeInBrick getAttributeInBrick(HAPBrick brick, String attrName) {
		HAPAttributeInBrick out = null;
		for(HAPAttributeInBrick attr : brick.getAttributes()) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return out;
	}

	public static HAPInfoValuePortContainer getDescdentValuePortContainerInfo(HAPBundle bundle, HAPPath path, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick brick = null;
		HAPDomainValueStructure valueStructureDomain = null;
		HAPResultBrick brickResult = getDescdentResultWithLocalBrick(bundle, path);
		if(brickResult.isInternalBrick()) {
			brick = brickResult.getBrick();
			valueStructureDomain = bundle.getValueStructureDomain();
		}
		else {
			HAPResourceDataBrick resourceData =(HAPResourceDataBrick)HAPUtilityResource.getResource(brickResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
			brick = resourceData.getBrick();
			valueStructureDomain = resourceData.getValueStructureDomain();
		}
		return new HAPInfoValuePortContainer(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), valueStructureDomain, brickResult.isInternalBrick());
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
	
	public static HAPBrick getBrickByResource(HAPInfoResourceIdNormalize normalizedResourceId, HAPManagerApplicationBrick brickMan) {
		HAPBundle bundle = getBrickBundle(normalizedResourceId.getRootResourceId(), brickMan);
		return getDescdentBrickLocal(bundle.getMainBrickWrapper(), normalizedResourceId.getPath());
	}
	
	public static HAPBrick getDescdentBrick(HAPBundle bundle, HAPPath path, HAPManagerApplicationBrick brickManager) {
		return getDescdentResultWithBrick(bundle, path, brickManager).getBrick();
	}

	
	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPIdBrickInBundle brickInBundleId) {
		return getDescdentBrickLocal(bundle.getMainBrickWrapper(), new HAPPath(brickInBundleId.getIdPath()));
	}

	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPPath path) {
		return getDescdentBrickLocal(bundle.getMainBrickWrapper(), path);
	}

	public static HAPBrick getDescdentBrickLocal(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		return getDescdentBrickLocal(rootBrickWrapper.getBrick(), path);
	}

	public static HAPBrick getDescdentBrickLocal(HAPBrick brick, HAPPath path) {
		HAPResultBrick brickResult = getDescendantBrickResult(brick, path);
		if(brickResult!=null) {
			return brickResult.getBrick();
		}
		return null;
	}

	public static HAPResultBrick getDescdentResultWithBrick(HAPBundle bundle, HAPPath path, HAPManagerApplicationBrick brickManager) {
		return getDescdentResultWithBrick(bundle.getMainBrickWrapper().getBrick(), path, brickManager);
	}
	
	public static HAPResultBrick getDescdentResultWithBrick(HAPBrick brick, HAPPath path, HAPManagerApplicationBrick brickManager) {
		HAPResultBrick brickResult = getDescendantBrickResult(brick, new HAPPath(path));
		if(brickResult.getBrick()==null) {
			brickResult.setBrick(HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId(brickResult.getResourceId()), brickManager));			
		}
		return brickResult;	
	}
	
	public static HAPResultBrick getDescdentResultWithLocalBrick(HAPBundle bundle, HAPPath path) {
		HAPResultBrick brickResult =  getDescdentBrickResult(bundle.getMainBrickWrapper(), path);
		return brickResult;
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

}
