package com.nosliw.data.core.domain.common.script;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginEntityProcessorSimpleScriptBased extends HAPPluginProcessorBrickDefinitionSimpleImp{

	public HAPPluginEntityProcessorSimpleScriptBased(String entityType) {
		super(entityType, HAPExecutableEntityScriptBasedSimple.class);
	}
 
	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualBrick entityDef, HAPContextProcessor processContext) {
		HAPExecutableEntityScriptBasedSimple scriptBasedEntityExe = (HAPExecutableEntityScriptBasedSimple)entityExe;
		HAPDefinitionEntityScriptBasedSimple scriptBasedEntityDef = (HAPDefinitionEntityScriptBasedSimple)entityDef;
		scriptBasedEntityExe.setScript(scriptBasedEntityDef.getScript());
		scriptBasedEntityExe.setResrouceId(scriptBasedEntityDef.getScriptResourceId());
	}
}
