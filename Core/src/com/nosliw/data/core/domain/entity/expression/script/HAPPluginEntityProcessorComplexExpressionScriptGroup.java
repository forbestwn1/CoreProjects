package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexExpressionScriptGroup extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionScriptGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPExecutableEntityExpressionScriptGroup.class);
	}

	@Override
	public void init(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

	}
	
	//matcher
	@Override
	public void postProcessValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
	}
	
	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionScriptGroup executableExpresionGroup = (HAPExecutableEntityExpressionScriptGroup)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityExpressionScriptGroup expressionGroupDef = (HAPDefinitionEntityExpressionScriptGroup)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		for(HAPDefinitionExpression expressionDef : expressionGroupDef.getEntityElements()) {
			HAPExecutableExpression expressionExe = HAPUtilityExpressionExecute.processExpression(expressionDef, executableExpresionGroup, processContext, new HAPGeneratorId());
			executableExpresionGroup.addExpressionItem(expressionExe);
		}
	}
}
