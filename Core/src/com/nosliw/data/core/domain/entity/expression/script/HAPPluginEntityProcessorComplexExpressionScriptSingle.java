package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexExpressionScriptSingle extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionScriptSingle() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, HAPExecutableEntityExpressionScriptSingle.class);
	}

	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionScriptSingle executableExpresionSingle = (HAPExecutableEntityExpressionScriptSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityExpressionScriptSingle expressionSingleDef = (HAPDefinitionEntityExpressionScriptSingle)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		HAPExecutableExpression expressionExe = HAPUtilityScriptExpressionExecute.processExpression(expressionSingleDef.getExpression(), executableExpresionSingle, processContext, new HAPGeneratorId());
		executableExpresionSingle.setExpression(expressionExe);
	}
}
