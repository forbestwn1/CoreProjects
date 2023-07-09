package com.nosliw.data.core.domain.entity.expression;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPUtilityOperand;

public class HAPPluginEntityProcessorComplexExpressionSingle extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionSingle() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, HAPExecutableEntityExpressionSingle.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionSingle executableExpresion = (HAPExecutableEntityExpressionSingle)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueContext = executableExpresion.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionSingle definitionExpression = (HAPDefinitionEntityExpressionSingle)defEntityInfo.getEntity();

		//process referenced expression
		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//process mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(complexEntityExecutableId, processContext);
		
		//build expression in executable
		executableExpresion.setExpression(new HAPExecutableExpression(definitionExpression.getExpression()));
		
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
		buildVariableInfoInExpression(complexEntityExecutableId, processContext);

	}

	//build variable into within expression item
	private static void buildVariableInfoInExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableEntityExpressionGroup expressionGroupExe = (HAPExecutableEntityExpressionGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		HAPContainerVariableCriteriaInfo expressionGroupVarsContainer = expressionGroupExe.getVariablesInfo();
		List<HAPExecutableExpression> items = expressionGroupExe.getAllExpressionItems();
		for(HAPExecutableExpression item : items) {
			Set<String> varKeys = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			for(String varKey : varKeys) {
				item.addVariableKey(varKey);
			}
		}
	}
	
}
