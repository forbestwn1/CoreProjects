package com.nosliw.data.core.entity;

import com.nosliw.common.path.HAPPath;

public class HAPInfoEntity implements HAPTreeNode{

	private HAPEntityExecutable m_entityExe;

	@Override
	public HAPEntityExecutable getEntity() {   return this.m_entityExe;     }
	public void setEntity(HAPEntityExecutable entity) {     this.m_entityExe = entity;     }

	@Override
	public HAPPath getPathFromRoot() {  return null;  }
	
}
