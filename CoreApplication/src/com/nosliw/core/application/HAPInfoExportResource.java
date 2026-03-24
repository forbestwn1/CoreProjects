package com.nosliw.core.application;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.path.HAPPath;

public class HAPInfoExportResource extends HAPEntityInfoImp{

	private HAPPath m_pathFromRoot;
	
	public HAPInfoExportResource(HAPPath pathFromRoot) {
		this.m_pathFromRoot = pathFromRoot;
	}
	
	public HAPPath getPathFromRoot() {     return this.m_pathFromRoot;      }
	
}
