package com.nosliw.data.core.interactive;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginSimpleEntityProcessorInteractive extends HAPPluginProcessorBrickDefinitionSimpleImp{

	public HAPPluginSimpleEntityProcessorInteractive(String entityType) {
		super(entityType, HAPExecutableEntityInteractive.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualBrick entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityInteractive interactiveExe = (HAPExecutableEntityInteractive)entityExe;
		HAPDefinitionEntityInteractive interactiveDef = (HAPDefinitionEntityInteractive)entityDef;
		interactiveExe.setInteractive(interactiveDef.getInteractive());
	}

}
