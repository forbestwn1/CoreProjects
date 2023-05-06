package com.nosliw.data.core.domain.entity.test.decoration.script;

import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPPluginComplexEntityProcessorTestDecoration1 extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestDecoration1() {
		super(HAPExecutableTestDecoration1.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentPackage = processContext.getCurrentBundle();
		
		HAPExecutableTestDecoration1 executableEntity = (HAPExecutableTestDecoration1)currentPackage.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentPackage.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestDecoration1 definitionEntity = (HAPDefinitionEntityTestDecoration1)currentPackage.getDefinitionDomain().getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		executableEntity.setScript(definitionEntity.getScript());	
	}

}
