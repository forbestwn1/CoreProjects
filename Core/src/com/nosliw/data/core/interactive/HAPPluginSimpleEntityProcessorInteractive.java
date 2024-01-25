package com.nosliw.data.core.interactive;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorSimpleImp;

public class HAPPluginSimpleEntityProcessorInteractive extends HAPPluginEntityProcessorSimpleImp{

	public HAPPluginSimpleEntityProcessorInteractive(String entityType) {
		super(entityType, HAPExecutableEntityInteractive.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPDefinitionEntityInDomain entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityInteractive interactiveExe = (HAPExecutableEntityInteractive)entityExe;
		HAPDefinitionEntityInteractive interactiveDef = (HAPDefinitionEntityInteractive)entityDef;
		interactiveExe.setInteractive(interactiveDef.getInteractive());
	}

}
