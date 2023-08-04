package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableEntityExpressionDataGroup;

public class HAPPluginEntityProcessorComplexExpressionDataGroup extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionDataGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPExecutableEntityExpressionScriptGroup.class);
	}

	@Override
	public void init(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionDataGroup executableExpresionGroup = (HAPExecutableEntityExpressionDataGroup)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionDataGroup definitionExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)defEntityInfo.getEntity();
		
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

	}
	
	//matcher
	@Override
	public void postProcessValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionDataGroup executableExpresionGroup = (HAPExecutableEntityExpressionDataGroup)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		
	}
	
	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

	}

}
