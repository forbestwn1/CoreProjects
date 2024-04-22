package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownwardImpTreeNode extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrick rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
		return this.processTreeNode(childTreeNode, data);
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrick rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
		this.postProcessTreeNode(childTreeNode, data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	protected abstract boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data);
	
	//after process attribute
	protected void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {}

}
