package com.nosliw.data.core.domain.entity.scriptbased;

import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPPluginComplexEntityProcessorScript extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorScript(String entityType) {
		super(entityType, HAPExecutableEntityScriptComplex.class);
	}
 
	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityScriptComplex definitionEntity = (HAPDefinitionEntityScriptComplex)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		HAPExecutableEntityScriptComplex executableEntity = (HAPExecutableEntityScriptComplex)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		executableEntity.setScript(definitionEntity.getScript());
	}

}
