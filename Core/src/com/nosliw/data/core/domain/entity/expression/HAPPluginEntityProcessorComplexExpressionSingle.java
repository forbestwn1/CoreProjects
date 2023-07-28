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
	public void init(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionSingle executableExpresion = (HAPExecutableEntityExpressionSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionSingle definitionExpression = (HAPDefinitionEntityExpressionSingle)defEntityInfo.getEntity();

		HAPUtilityExpressionProcessor.buildEntityExpressionExe(executableExpresion, definitionExpression);
		
		//build expression in executable
		executableExpresion.setExpression(new HAPExecutableExpression(definitionExpression.getExpression()));
		
		//build constant value for expression
		HAPUtilityExpressionProcessor.processConstant(executableExpresion, executableExpresion.getExpression(), processContext);
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		HAPExecutableEntityExpressionSingle executableExpresion = (HAPExecutableEntityExpressionSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueContext = executableExpresion.getValueContext();
		
		//resolve variable name
		HAPUtilityExpressionProcessor.resolveVariableName(executableExpresion.getExpression(), valueContext, executableExpresion.getVariablesInfo(), valueStructureDomain);
		
	}
	
	//matcher
	@Override
	public void postProcessValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionSingle executableExpresion = (HAPExecutableEntityExpressionSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		
		//build all variables info in expression group
		HAPUtilityExpressionProcessor.buildVariableInfo(executableExpresion.getVariablesInfo(), valueStructureDomain);
		
		//resolve variable name in mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(complexEntityExecutableId, processContext);

		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresion.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		
			//update value context according to variable info
			HAPUtilityExpressionProcessor.updateValueContext(executableExpresion.getVariablesInfo(), valueStructureDomain);
		}
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		//process referenced expression
//		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//build variable into within expression item
		HAPUtilityExpressionProcessor.buildVariableInfoInExpression(complexEntityExecutableId, processContext);
	}
}
