package com.nosliw.core.application.division.manual.common.task;

import com.nosliw.core.application.common.interactive1.HAPBrickInteractive;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionSimpleImp;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginSimpleEntityProcessorInteractive extends HAPPluginProcessorBrickDefinitionSimpleImp{

	public HAPPluginSimpleEntityProcessorInteractive(String entityType) {
		super(entityType, HAPBrickInteractive.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualDefinitionBrick entityDef, HAPContextProcessor processContext) {
		HAPBrickInteractive interactiveExe = (HAPBrickInteractive)entityExe;
		HAPManualBrickInteractive interactiveDef = (HAPManualBrickInteractive)entityDef;
		interactiveExe.setExpressionInteractive(interactiveDef.getInteractive());
	}

}
