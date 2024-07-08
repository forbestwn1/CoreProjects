package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPPath;

public class HAPInfoTreeNode {

	private HAPPath m_pathFromRoot;
	
	private HAPManualBrick m_parent;

	public HAPInfoTreeNode() {
		this(new HAPPath(), null);
	}

	public HAPInfoTreeNode(HAPPath pathFromRoot, HAPManualBrick parent) {
		this.m_pathFromRoot = pathFromRoot;
		this.m_parent = parent;
	}
	
	public HAPPath getPathFromRoot() {   return this.m_pathFromRoot;     }
	
	public HAPManualBrick getParent() {   return this.m_parent;    }
	
}
