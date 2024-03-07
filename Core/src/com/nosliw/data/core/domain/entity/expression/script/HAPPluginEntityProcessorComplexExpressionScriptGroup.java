package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;

public class HAPPluginEntityProcessorComplexExpressionScriptGroup extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexExpressionScriptGroup(String entityType) {
		super(entityType, HAPExecutableEntityExpressionScriptGroup.class);
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionScriptGroup executableExpresionGroup = (HAPExecutableEntityExpressionScriptGroup)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPDefinitionEntityExpressionScriptGroup expressionGroupDef = (HAPDefinitionEntityExpressionScriptGroup)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		for(HAPDefinitionExpression expressionDef : expressionGroupDef.getEntityElements()) {
			HAPExecutableExpression expressionExe = HAPUtilityScriptExpressionExecute.processExpression(expressionDef, executableExpresionGroup, processContext, new HAPGeneratorId());
			executableExpresionGroup.addExpressionItem(expressionExe);
		}
	}
}
