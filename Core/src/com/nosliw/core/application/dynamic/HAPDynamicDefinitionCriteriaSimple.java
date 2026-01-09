package com.nosliw.core.application.dynamic;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;

public class HAPDynamicDefinitionCriteriaSimple extends HAPDynamicDefinitionCriteria{

	public final static String CRITERIA = "criteria"; 

	private HAPCriteriaBrick m_brickCriteria;

	public HAPDynamicDefinitionCriteriaSimple() {
		super(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE);
	}

	public HAPCriteriaBrick getBrickCriteria() {	return this.m_brickCriteria;	}
	
	public void setBrickCriteria(HAPCriteriaBrick brickCriteria) {    this.m_brickCriteria = brickCriteria;       }
	
}
