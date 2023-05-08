package com.nosliw.data.core.domain.entity.decoration.script1;

import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPDefinitionEntityTestComplexScript;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPExecutableTestComplexScript;

public class HAPPluginComplexEntityProcessorDecorationScript extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorDecorationScript() {
		super(HAPExecutableDecorationScript.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		executableEntity.setScriptName(definitionEntity.getScriptName());
	}

}
