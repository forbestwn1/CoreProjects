package com.nosliw.data.core.entity;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;

public abstract class HAPProcessorEntityExecutableDownwardImpTreeNode extends HAPProcessorEntityExecutableDownward{

	@Override
	public boolean processEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNode childTreeNode = HAPUtilityEntity.getDescdentTreeNode(rootEntityInfo, path);
		return this.processTreeNode(childTreeNode, data);
	}

	@Override
	public void postProcessEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		HAPTreeNode childTreeNode = HAPUtilityEntity.getDescdentTreeNode(rootEntityInfo, path);
		this.postProcessTreeNode(childTreeNode, data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	protected abstract boolean processTreeNode(HAPTreeNode treeNode, Object data);
	
	//after process attribute
	protected void postProcessTreeNode(HAPTreeNode treeNode, Object data) {}

}
