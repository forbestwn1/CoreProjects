package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownwardImpAttribute extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.processRootEntity(rootEntity, data);
			return true;
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			return processAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.postProcessRootEntity(rootEntity, data);
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			postProcessAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	public abstract void processRootEntity(HAPBrick rootEntity, Object data);
	
	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPBrick parentEntity, String attribute, Object data);
	
	//after process attribute
	public void postProcessAttribute(HAPBrick parentEntity, String attribute, Object data) {}
	
	public void postProcessRootEntity(HAPBrick rootEntity, Object data) {}
}
