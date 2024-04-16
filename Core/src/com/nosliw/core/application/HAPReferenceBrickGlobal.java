package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public class HAPReferenceBrickGlobal {

	//which bundle this brick belong
	private HAPBundle m_bundle;
	//path from root in this bundle
	private HAPPath m_pathFromRoot;

	public HAPReferenceBrickGlobal(HAPBundle bundle, HAPPath pathFromRoot) {
		this.m_bundle = bundle;
		this.m_pathFromRoot = pathFromRoot;
	}

	public HAPBundle getBundle() {    return this.m_bundle;    }
	public HAPPath getPathFromRoot() {    return this.m_pathFromRoot;     }
}
