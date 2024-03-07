package com.nosliw.data.core.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPProcessorEntityExecutableDownwardImpAttribute extends HAPProcessorEntityExecutableDownward{

	@Override
	public boolean processEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.processRootEntity(rootEntity, data);
			return true;
		}
		else {
			Pair<HAPEntityExecutable, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			return processAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	@Override
	public void postProcessEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.postProcessRootEntity(rootEntity, data);
		}
		else {
			Pair<HAPEntityExecutable, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			postProcessAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), data);
		}
	}

	public abstract void processRootEntity(HAPEntityExecutable rootEntity, Object data);
	
	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPEntityExecutable parentEntity, String attribute, Object data);
	
	//after process attribute
	public void postProcessAttribute(HAPEntityExecutable parentEntity, String attribute, Object data) {}
	
	public void postProcessRootEntity(HAPEntityExecutable rootEntity, Object data) {}
}
