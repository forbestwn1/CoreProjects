package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPWithBrick;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.division.manual.HAPManualWrapperBrickRoot;

public abstract class HAPHandlerDownwardImpTreeNode extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPManualExeUtilityBrick.getDescdentTreeNode((HAPManualWrapperBrickRoot)rootEntityInfo, path);
		return this.processTreeNode(childTreeNode, data);
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = HAPManualExeUtilityBrick.getDescdentTreeNode((HAPManualWrapperBrickRoot)rootEntityInfo, path);
		this.postProcessTreeNode(childTreeNode, data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	protected abstract boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data);
	
	//after process attribute
	protected void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {}

	protected HAPManualBrick getBrickFromNode(HAPTreeNodeBrick node) {
		HAPManualBrick out = null;
		Object value = node.getNodeValue();
		if(value instanceof HAPBrick) {
			out = (HAPManualBrick)value;
		}
		else if(value instanceof HAPWithBrick){
			out = (HAPManualBrick)((HAPWithBrick)value).getBrick();
		}
		return out;
	}

}
