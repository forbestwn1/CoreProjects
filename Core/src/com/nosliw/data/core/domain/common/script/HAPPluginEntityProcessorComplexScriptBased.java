package com.nosliw.data.core.domain.common.script;

import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public class HAPPluginEntityProcessorComplexScriptBased extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexScriptBased(String entityType) {
		super(entityType, HAPExecutableEntityScriptBasedComplex.class);
	}
 
	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPDefinitionEntityScriptBasedComplex definitionEntity = (HAPDefinitionEntityScriptBasedComplex)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		HAPExecutableEntityScriptBasedComplex executableEntity = (HAPExecutableEntityScriptBasedComplex)complexEntityExecutable;
		executableEntity.setScript(definitionEntity.getScript());
	}

}
