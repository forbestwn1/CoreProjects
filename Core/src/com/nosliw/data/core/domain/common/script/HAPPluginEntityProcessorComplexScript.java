package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexScript extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexScript(String entityType) {
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
