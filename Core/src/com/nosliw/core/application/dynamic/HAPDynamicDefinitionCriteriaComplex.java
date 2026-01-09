package com.nosliw.core.application.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDynamicDefinitionCriteriaComplex extends HAPDynamicDefinitionCriteria{

	private List<HAPDynamicDefinitionCriteria> m_children;

	public HAPDynamicDefinitionCriteriaComplex() {
		super(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_COMPLEX);
		this.m_children = new ArrayList<HAPDynamicDefinitionCriteria>();
	}

	public List<HAPDynamicDefinitionCriteria> getChildren(){
		return this.m_children;
	}
}
