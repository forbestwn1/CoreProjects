package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public abstract class HAPPluginProcessorBlockSimpleImp implements HAPPluginProcessorBlockSimple{

	private HAPIdBrickType m_brickTypeId;

	public HAPPluginProcessorBlockSimpleImp(HAPIdBrickType brickTypeId) {
		this.m_brickTypeId = brickTypeId;
	}
	
	@Override
	public HAPIdBrickType getBrickType() {
		return this.m_brickTypeId;
	}

	@Override
	public void postProcess(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {}
	
}
