package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexScriptBased extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexScriptBased(String entityType) {
		super(entityType, HAPExecutableEntityScriptBasedComplex.class);
	}
 
	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityScriptBasedComplex definitionEntity = (HAPDefinitionEntityScriptBasedComplex)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		HAPExecutableEntityScriptBasedComplex executableEntity = (HAPExecutableEntityScriptBasedComplex)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		executableEntity.setScript(definitionEntity.getScript());
	}

}
