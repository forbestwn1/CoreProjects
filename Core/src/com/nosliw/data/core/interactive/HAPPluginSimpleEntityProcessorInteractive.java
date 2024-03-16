package com.nosliw.data.core.interactive;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginSimpleEntityProcessorInteractive extends HAPPluginProcessorEntityDefinitionSimpleImp{

	public HAPPluginSimpleEntityProcessorInteractive(String entityType) {
		super(entityType, HAPExecutableEntityInteractive.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualEntity entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityInteractive interactiveExe = (HAPExecutableEntityInteractive)entityExe;
		HAPDefinitionEntityInteractive interactiveDef = (HAPDefinitionEntityInteractive)entityDef;
		interactiveExe.setInteractive(interactiveDef.getInteractive());
	}

}
