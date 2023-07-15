package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;

public class HAPPluginEntityProcessorComplexExpressionSingle extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionSingle() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, HAPExecutableEntityExpressionSingle.class);
	}

	@Override
	public void postProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionSingle executableExpresion = (HAPExecutableEntityExpressionSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueContext = executableExpresion.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionSingle definitionExpression = (HAPDefinitionEntityExpressionSingle)defEntityInfo.getEntity();

		HAPUtilityExpressionProcessor.buildEntityExpressionExe(executableExpresion, definitionExpression);
		
		//build expression in executable
		executableExpresion.setExpression(new HAPExecutableExpression(definitionExpression.getExpression()));
		
		//process referenced expression
		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//process mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(complexEntityExecutableId, processContext);
		
		//build constant value for expression
		HAPUtilityExpressionProcessor.processConstant(executableExpresion, executableExpresion.getExpression(), processContext);
		
		//replace variable name with id
		HAPUtilityExpressionProcessor.resolveVariableName(executableExpresion.getExpression(), valueContext, executableExpresion.getVariablesInfo(), valueStructureDomain);
		
		//build all variables info in expression group
		HAPUtilityExpressionProcessor.buildVariableInfo(executableExpresion.getVariablesInfo(), valueStructureDomain);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresion.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		}
		
		//build variable into within expression item
		HAPUtilityExpressionProcessor.buildVariableInfoInExpression(complexEntityExecutableId, processContext);

	}

	
}
