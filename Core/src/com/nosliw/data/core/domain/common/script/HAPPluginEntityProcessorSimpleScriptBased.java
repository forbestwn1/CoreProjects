package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorSimpleImp;

public class HAPPluginEntityProcessorSimpleScriptBased extends HAPPluginEntityProcessorSimpleImp{

	public HAPPluginEntityProcessorSimpleScriptBased(String entityType) {
		super(entityType, HAPExecutableEntityScriptBasedSimple.class);
	}
 
	@Override
	protected void process(HAPExecutableEntity entityExe, HAPDefinitionEntityInDomain entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityScriptBasedSimple scriptBasedEntityExe = (HAPExecutableEntityScriptBasedSimple)entityExe;
		HAPDefinitionEntityScriptBasedSimple scriptBasedEntityDef = (HAPDefinitionEntityScriptBasedSimple)entityDef;
		scriptBasedEntityExe.setScript(scriptBasedEntityDef.getScript());
		scriptBasedEntityExe.setResrouceId(scriptBasedEntityDef.getScriptResourceId());
	}
}
