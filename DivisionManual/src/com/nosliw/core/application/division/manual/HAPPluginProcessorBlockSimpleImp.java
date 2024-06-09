package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;

public abstract class HAPPluginProcessorBlockSimpleImp implements HAPPluginProcessorBlockSimple{

	private HAPIdBrickType m_brickTypeId;

	public HAPPluginProcessorBlockSimpleImp(HAPIdBrickType brickTypeId) {
		this.m_brickTypeId = brickTypeId;
	}
	
	@Override
	public HAPIdBrickType getBrickType() {
		return this.m_brickTypeId;
	}

}
