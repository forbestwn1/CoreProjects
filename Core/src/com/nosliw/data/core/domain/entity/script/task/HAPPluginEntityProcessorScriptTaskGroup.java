package com.nosliw.data.core.domain.entity.script.task;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public class HAPPluginEntityProcessorScriptTaskGroup extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorScriptTaskGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, HAPExecutableEntityScriptTaskGroup.class);
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityScriptTaskGroup scriptTaskGroupExe = (HAPExecutableEntityScriptTaskGroup)complexEntityExecutable;
		
		scriptTaskGroupExe.setScript(scriptTaskGroupDef.getScript());
		
		for(HAPDefinitionTaskScript taskScript : scriptTaskGroupDef.getDefinitions()) {
			scriptTaskGroupExe.addDefinition(taskScript);
		}
	}
}
