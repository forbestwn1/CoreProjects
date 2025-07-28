package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public class HAPPluginEntityProcessorComplexExpressionScriptGroup extends HAPPluginProcessorBrickDefinitionComplexImp{

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
			HAPExpressionScript expressionExe = HAPUtilityScriptExpressionExecute.processExpression(expressionDef, executableExpresionGroup, processContext, new HAPGeneratorId());
			executableExpresionGroup.addExpressionItem(expressionExe);
		}
	}
}
