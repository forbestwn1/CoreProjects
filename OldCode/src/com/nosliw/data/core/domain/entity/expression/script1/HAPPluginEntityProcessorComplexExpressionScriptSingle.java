package com.nosliw.data.core.domain.entity.expression.script1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public class HAPPluginEntityProcessorComplexExpressionScriptSingle extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexExpressionScriptSingle() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, HAPExecutableEntityExpressionScriptSingle.class);
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionScriptSingle executableExpresionSingle = (HAPExecutableEntityExpressionScriptSingle)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPDefinitionEntityExpressionScriptSingle expressionSingleDef = (HAPDefinitionEntityExpressionScriptSingle)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		HAPExecutableExpression expressionExe = HAPUtilityScriptExpressionExecute.processExpression(expressionSingleDef.getExpression(), executableExpresionSingle, processContext, new HAPGeneratorId());
		executableExpresionSingle.setExpression(expressionExe);
	}
}
