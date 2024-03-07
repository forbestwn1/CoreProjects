package com.nosliw.data.core.entity;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public class HAPAttributeExecutable extends HAPExecutableImpEntityInfo implements HAPTreeNode{

	private HAPInfoAttributeValue m_valueInfo;
	
	//path from root
	private HAPPath m_pathFromRoot;

	public HAPInfoAttributeValue getValueInfo() {	return this.m_valueInfo;	}
	
	public void setValueInfo(HAPInfoAttributeValue valueInfo) {
		
	}

	@Override
	public HAPPath getPathFromRoot() {    return this.m_pathFromRoot;    }


}
