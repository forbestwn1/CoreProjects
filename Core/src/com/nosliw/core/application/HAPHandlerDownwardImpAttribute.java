package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownwardImpAttribute extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.processRootEntity(HAPUtilityBrick.getDescdentBrickLocal(bundle, path), data);
			return true;
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(bundle, path);
			return processAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	@Override
	public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.postProcessRootEntity(HAPUtilityBrick.getDescdentBrickLocal(bundle, path), data);
		}
		else {
			Pair<HAPBrick, String> parentAttrInfo = getParentAttributeInfo(bundle, path);
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
