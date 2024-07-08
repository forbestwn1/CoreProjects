package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrick;

public class HAPManualDefinitionWrapperValueReferenceBrick extends HAPManualDefinitionWrapperValueWithBrick{

	//reference to attachment
	public static final String BRICKREFERENCE = "brickId";

	private HAPIdBrick m_localBrickId;
	
	public HAPManualDefinitionWrapperValueReferenceBrick(HAPIdBrick localBrickId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICKREFERENCE, localBrickId.getBrickTypeId());
		this.m_localBrickId = localBrickId;
	}

	public HAPIdBrick getLocalBrickId() {     return this.m_localBrickId;     }

}
