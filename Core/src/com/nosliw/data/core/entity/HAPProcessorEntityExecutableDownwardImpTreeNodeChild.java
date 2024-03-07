package com.nosliw.data.core.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPProcessorEntityExecutableDownwardImpTreeNodeChild extends HAPProcessorEntityExecutableDownward{

	@Override
	public boolean processEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		Pair<HAPTreeNode, String> treeLeafInfo = this.getNodePathInfo(rootEntityInfo, path); 
		return processChildLeaf(treeLeafInfo.getLeft(), treeLeafInfo.getRight(), data);
	}

	@Override
	public void postProcessEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		Pair<HAPTreeNode, String> treeLeafInfo = this.getNodePathInfo(rootEntityInfo, path); 
		postProcessChildLeaf(treeLeafInfo.getLeft(), treeLeafInfo.getRight(), data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processChildLeaf(HAPTreeNode treeNode, String attribute, Object data);
	
	//after process attribute
	public void postProcessChildLeaf(HAPTreeNode treeNode, String attribute, Object data) {}

}
