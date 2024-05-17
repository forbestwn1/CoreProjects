package com.nosliw.core.application.division.manual.common.interactive;

import com.nosliw.core.application.common.interactive1.HAPBrickInteractive;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginSimpleEntityProcessorInteractive extends HAPPluginProcessorBrickDefinitionSimpleImp{

	public HAPPluginSimpleEntityProcessorInteractive(String entityType) {
		super(entityType, HAPBrickInteractive.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualBrick entityDef, HAPContextProcessor processContext) {
		HAPBrickInteractive interactiveExe = (HAPBrickInteractive)entityExe;
		HAPManualBrickInteractive interactiveDef = (HAPManualBrickInteractive)entityDef;
		interactiveExe.setInteractive(interactiveDef.getInteractive());
	}

}
