package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public class HAPInfoTreeNode {

	private HAPPath m_pathFromRoot;
	
	private HAPBrick m_parent;
	
	public HAPInfoTreeNode(HAPPath pathFromRoot, HAPBrick parent) {
		this.m_pathFromRoot = pathFromRoot;
		this.m_parent = parent;
	}
	
	public HAPPath getPathFromRoot() {   return this.m_pathFromRoot;     }
	
	public HAPBrick getParent() {   return this.m_parent;    }
	
}
