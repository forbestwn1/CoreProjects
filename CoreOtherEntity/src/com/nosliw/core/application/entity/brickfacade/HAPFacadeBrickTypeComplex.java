package com.nosliw.core.application.entity.brickfacade;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

//facade combo
public class HAPFacadeBrickTypeComplex extends HAPFacadeBrickType{

	private List<HAPFacadeBrickType> m_children;
	
	public HAPFacadeBrickTypeComplex() {
		super(HAPConstantShared.BRICKFACADE_TYPE_COMPLEX);
		this.m_children = new ArrayList<HAPFacadeBrickType>();
	}
	
	public HAPFacadeBrickTypeComplex(String name, List<HAPFacadeBrickType> facades) {
		this();
		this.setName(name);
		this.m_children.addAll(facades);
	}

	
}
