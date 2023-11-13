package com.nosliw.data.core.domain.entity.script.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorScriptTaskGroup extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorScriptTaskGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, HAPExecutableEntityScriptTaskGroup.class);
	}

	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> pair = this.getEntityPair(complexEntityExecutableId, processContext);
		
		HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)pair.getLeft();
		HAPExecutableEntityScriptTaskGroup scriptTaskGroupExe = (HAPExecutableEntityScriptTaskGroup)pair.getRight();
		
		scriptTaskGroupExe.setScript(scriptTaskGroupDef.getScript());
		
		for(HAPDefinitionTaskScript taskScript : scriptTaskGroupDef.getDefinitions()) {
			scriptTaskGroupExe.addDefinition(taskScript);
		}
	}
}
