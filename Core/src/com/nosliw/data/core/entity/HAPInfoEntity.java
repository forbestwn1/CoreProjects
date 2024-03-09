package com.nosliw.data.core.entity;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;

public class HAPInfoEntity implements HAPTreeNode, HAPWithEntity{

	private HAPEntityExecutable m_entityExe;

	public HAPInfoEntity(HAPEntityExecutable entity) {
		this.m_entityExe = entity;
	}
	
	@Override
	public HAPEntityExecutable getEntity() {   return this.m_entityExe;     }
	public void setEntity(HAPEntityExecutable entity) {     this.m_entityExe = entity;     }

	@Override
	public HAPPath getPathFromRoot() {  return null;  }

	@Override
	public Object getNodeValue() {  return this.getEntity();   }
	
}
