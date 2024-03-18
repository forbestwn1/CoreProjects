package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrick;

public class HAPManualWrapperValueInAttributeReferenceBrick extends HAPManualWrapperValueInAttributeWithBrick{

	//reference to attachment
	public static final String BRICKREFERENCE = "brickReference";

	private HAPIdBrick m_localBrickId;
	
	public HAPManualWrapperValueInAttributeReferenceBrick(HAPIdBrick localBrickId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICKREFERENCE, localBrickId.getBrickTypeId());
		this.m_localBrickId = localBrickId;
	}

	public HAPIdBrick getLocalBrickId() {     return this.m_localBrickId;     }

}
