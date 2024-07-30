package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownwardImpAttribute extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.processRootEntity(rootBrickWrapper.getBrick(), data);
			return true;
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(rootBrickWrapper, path);
			return processAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.postProcessRootEntity(rootBrickWrapper.getBrick(), data);
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(rootBrickWrapper, path);
			postProcessAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	public abstract void processRootEntity(HAPBrick rootEntity, Object data);
	
	public void postProcessRootEntity(HAPBrick rootEntity, Object data) {}

	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPBrick parentBrick, String attributeName, Object data);
	
	//after process attribute
	public void postProcessAttribute(HAPBrick parentBrick, String attributeName, Object data) {}
	
}
