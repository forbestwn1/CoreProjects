package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPWrapperBrickRoot;

public abstract class HAPHandlerDownwardImpTreeNode extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
		return this.processTreeNode(childTreeNode, data);
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
		this.postProcessTreeNode(childTreeNode, data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	protected abstract boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data);
	
	//after process attribute
	protected void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {}

}
