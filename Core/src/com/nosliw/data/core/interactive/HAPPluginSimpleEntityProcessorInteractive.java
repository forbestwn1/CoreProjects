package com.nosliw.data.core.interactive;

import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionSimpleImp;

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
