package com.nosliw.data.core.domain.entity.script.task;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorSimpleImp;

public class HAPPluginSimpleEntityProcessorScriptTaskGroup extends HAPPluginEntityProcessorSimpleImp{

	public HAPPluginSimpleEntityProcessorScriptTaskGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, HAPExecutableEntityScriptTaskGroup.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPDefinitionEntityInDomain entityDef, HAPContextProcessor processContext) {
		HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)entityDef;
		HAPExecutableEntityScriptTaskGroup scriptTaskGroupExe = (HAPExecutableEntityScriptTaskGroup)entityExe;
		
		scriptTaskGroupExe.setScript(scriptTaskGroupDef.getScript());
		
		for(HAPDefinitionTaskScript taskScript : scriptTaskGroupDef.getDefinitions()) {
			scriptTaskGroupExe.addDefinition(taskScript);
		}
	}
}
