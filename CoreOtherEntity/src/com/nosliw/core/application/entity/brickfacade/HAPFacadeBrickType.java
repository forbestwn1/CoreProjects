package com.nosliw.core.application.entity.brickfacade;

import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPFacadeBrickType extends HAPEntityInfoImp{

	public final static String STRUCTURETYPE = "structureType"; 
	
	private String m_structureType;
	
	public HAPFacadeBrickType(String structureType) {
		this.m_structureType = structureType;
	}
	
	public String getStructureType() {
		return this.m_structureType;
	}

}
