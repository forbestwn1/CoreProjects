package com.nosliw.core.application.division.manual.executable;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPWrapperBrickRoot;

public abstract class HAPHandlerDownwardImpTreeNodeChild extends HAPHandlerDownward{

	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		Pair<HAPTreeNode, String> treeLeafInfo = this.getNodePathInfo(rootEntityInfo, path); 
		return processChildLeaf(treeLeafInfo.getLeft(), treeLeafInfo.getRight(), data);
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootEntityInfo, HAPPath path, Object data) {
		Pair<HAPTreeNode, String> treeLeafInfo = this.getNodePathInfo(rootEntityInfo, path); 
		postProcessChildLeaf(treeLeafInfo.getLeft(), treeLeafInfo.getRight(), data);
	}

	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processChildLeaf(HAPTreeNode treeNode, String attribute, Object data);
	
	//after process attribute
	public void postProcessChildLeaf(HAPTreeNode treeNode, String attribute, Object data) {}

}
