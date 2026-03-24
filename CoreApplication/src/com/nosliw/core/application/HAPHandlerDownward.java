package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data);

//	public abstract boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

//	public abstract void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path.getLength()<=1;
	}

	protected Pair<HAPBrick, String> getParentAttributeInfo(HAPBundle bundle, HAPPath path) {
		HAPPath childPath = new HAPPath();
		String[] pathSegs = path.getPathSegments();
		for(int i=0; i<pathSegs.length-1; i++) {
			childPath = childPath.appendSegment(pathSegs[i]);
		}
		
		HAPBrick parentBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, childPath);
		return Pair.of(parentBrick, pathSegs[pathSegs.length-1]);
	}

	protected HAPBrick getChildBrick(HAPBrick parentBrick, String attributeName) {
		HAPAttributeInBrick attr = HAPUtilityBrick.getAttributeInBrick(parentBrick, attributeName);
		HAPWrapperValue attrValueWrapper = attr.getValueWrapper();
		String attrValueType = attrValueWrapper.getValueType();
		if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
			return ((HAPWrapperValueOfBrick)attrValueWrapper).getBrick();
		}
		else {
			return null;
		}
	}
}
