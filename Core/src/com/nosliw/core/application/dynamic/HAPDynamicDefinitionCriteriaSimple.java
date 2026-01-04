package com.nosliw.core.application.dynamic;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;

public class HAPDynamicDefinitionCriteriaSimple extends HAPSerializableImp implements HAPDynamicDefinitionCriteria{

	private HAPCriteriaBrick m_brickCriteria;

	@Override
	public String getCriteriaType() {   return HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE;   }

	public HAPCriteriaBrick getBrickCriteria() {
		return this.m_brickCriteria;
	}
	
}
