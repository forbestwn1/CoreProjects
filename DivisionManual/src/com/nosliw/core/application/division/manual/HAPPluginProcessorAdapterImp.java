package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;

public abstract class HAPPluginProcessorAdapterImp implements HAPPluginProcessorAdapter{

	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginProcessorAdapterImp(HAPIdBrickType entityType) {
		this.m_brickTypeId = entityType;
	}

	@Override
	public HAPIdBrickType getBrickType() {    return this.m_brickTypeId;    }

}
