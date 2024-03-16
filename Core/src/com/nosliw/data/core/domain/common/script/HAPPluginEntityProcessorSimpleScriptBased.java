package com.nosliw.data.core.domain.common.script;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginEntityProcessorSimpleScriptBased extends HAPPluginProcessorEntityDefinitionSimpleImp{

	public HAPPluginEntityProcessorSimpleScriptBased(String entityType) {
		super(entityType, HAPExecutableEntityScriptBasedSimple.class);
	}
 
	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualEntity entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityScriptBasedSimple scriptBasedEntityExe = (HAPExecutableEntityScriptBasedSimple)entityExe;
		HAPDefinitionEntityScriptBasedSimple scriptBasedEntityDef = (HAPDefinitionEntityScriptBasedSimple)entityDef;
		scriptBasedEntityExe.setScript(scriptBasedEntityDef.getScript());
		scriptBasedEntityExe.setResrouceId(scriptBasedEntityDef.getScriptResourceId());
	}
}
