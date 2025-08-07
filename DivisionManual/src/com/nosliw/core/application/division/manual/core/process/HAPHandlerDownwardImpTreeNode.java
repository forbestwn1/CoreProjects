package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWithBrick;
import com.nosliw.core.application.division.manual.core.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;

public abstract class HAPHandlerDownwardImpTreeNode extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = getDescdentTreeNode(bundle, path);
		return this.processTreeNode(childTreeNode, data);
	}

	@Override
	public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
		HAPTreeNodeBrick childTreeNode = getDescdentTreeNode(bundle, path);
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

	protected HAPTreeNodeBrick getDescdentTreeNode(HAPBundle bundle, HAPPath path) {
		HAPTreeNodeBrick out = null;

		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path);
		HAPManualWrapperBrickRoot rootWrapper = (HAPManualWrapperBrickRoot)bundle.getRootBrickWrapper(fullPathInfo.getRoot());
		if(fullPathInfo.getPath()==null || fullPathInfo.getPath().isEmpty()) {
			out = rootWrapper;
		}
		else {
			out = (HAPManualAttributeInBrick)HAPUtilityBrick.getDescendantAttribute(bundle, path);
		}
		return out;
	}
	
}
