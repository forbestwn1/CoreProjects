package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;

public class HAPPluginEntityProcessorComplexExpressionDataSingle extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexExpressionDataSingle() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, HAPExecutableEntityExpressionDataSingle.class);
	}

	@Override
	public void processValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionDataSingle executableExpresion = (HAPExecutableEntityExpressionDataSingle)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionDataSingle definitionExpression = (HAPDefinitionEntityExpressionDataSingle)defEntityInfo.getEntity();

		//build expression in executable
		executableExpresion.setExpression(new HAPExecutableExpressionData(definitionExpression.getExpression()));
		
		//build constant value for expression
		HAPUtilityExpressionProcessor.processConstant(executableExpresion, executableExpresion.getExpression(), processContext);
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		HAPExecutableEntityExpressionDataSingle executableExpresion = (HAPExecutableEntityExpressionDataSingle)complexEntityExecutable;
		HAPExecutableEntityValueContext valueContext = executableExpresion.getValueContext();
		
		//resolve variable name
		HAPUtilityExpressionProcessor.resolveVariableName(executableExpresion.getExpression(), valueContext, executableExpresion.getVariablesInfo(), valueStructureDomain);
		
	}
	
	//matcher
	@Override
	public void postProcessValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionDataSingle executableExpresion = (HAPExecutableEntityExpressionDataSingle)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		
		//build all variables info in expression group
		HAPUtilityExpressionProcessor.buildVariableInfo(executableExpresion.getVariablesInfo(), valueStructureDomain);
		
		//resolve variable name in mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(executableExpresion, processContext);

		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresion.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		
			//update value context according to variable info
			HAPUtilityExpressionProcessor.updateValueContext(executableExpresion.getVariablesInfo(), valueStructureDomain);
		}
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		//process referenced expression
//		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//build variable into within expression item
		HAPUtilityExpressionProcessor.buildVariableInfoInExpression((HAPExecutableEntityExpressionDataSingle)complexEntityExecutable, processContext);
	}
}
