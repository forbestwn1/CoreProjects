package com.nosliw.core.application.entity.brickfacade;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;

//facade combo
public class HAPFacadeBrickTypeComplex extends HAPEntityInfoImp implements HAPFacadeBrickType{

	private List<HAPFacadeBrickType> m_facades;
	
	public HAPFacadeBrickTypeComplex() {
		this.m_facades = new ArrayList<HAPFacadeBrickType>();
	}
	
	public HAPFacadeBrickTypeComplex(String name, List<HAPFacadeBrickType> facades) {
		this();
		this.m_facades.addAll(facades);
	}

	@Override
	public String getType() {
		return HAPConstantShared.BRICKFACADE_TYPE_COMPLEX;
	}
	
}
