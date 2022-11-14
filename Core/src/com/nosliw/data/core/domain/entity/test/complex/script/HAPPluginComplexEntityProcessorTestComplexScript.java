package com.nosliw.data.core.domain.entity.test.complex.script;

import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.script.HAPDefinitionEntityScript;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;

public class HAPPluginComplexEntityProcessorTestComplexScript extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestComplexScript() {
		super(HAPExecutableTestComplexScript.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPDomainEntityDefinitionGlobal globalDomain = processContext.getCurrentDefinitionDomain();
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityComplexValueStructure valueStructureComplex = executableEntity.getValueStructureComplex();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		HAPIdEntityInDomain scriptEntityId = definitionEntity.getNormalAttributeWithId(HAPDefinitionEntityTestComplexScript.ATTR_SCRIPT).getValue().getEntityId();
		HAPDefinitionEntityScript scriptDef = (HAPDefinitionEntityScript)globalDomain.getEntityInfoDefinition(scriptEntityId).getEntity();
		
		executableEntity.setScript(scriptDef.getScript());
		executableEntity.setParms(definitionEntity.getParms());
	}
}
