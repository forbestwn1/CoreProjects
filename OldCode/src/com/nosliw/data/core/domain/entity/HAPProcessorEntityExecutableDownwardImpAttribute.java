package com.nosliw.data.core.domain.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPProcessorEntityExecutableDownwardImpAttribute extends HAPProcessorEntityExecutableDownward{

	@Override
	public boolean processEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext) {
		if(this.isRoot(path)) {
			this.processRootEntity(rootEntity, processContext);
			return true;
		}
		else {
			Pair<HAPExecutableEntity, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			return processAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), processContext);
		}
	}

	@Override
	public void postProcessEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext) {
		if(this.isRoot(path)) {
			this.postProcessRootEntity(rootEntity, processContext);
		}
		else {
			Pair<HAPExecutableEntity, String> parentAttrInfo = getParentAttributeInfo(rootEntity, path);
			postProcessAttribute(parentAttrInfo.getLeft(), parentAttrInfo.getRight(), processContext);
		}
	}

	public abstract void processRootEntity(HAPExecutableEntity rootEntity, HAPContextProcessor processContext);
	
	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext);
	
	//after process attribute
	public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {}
	
	public void postProcessRootEntity(HAPExecutableEntity rootEntity, HAPContextProcessor processContext) {}
}
