package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionData;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityExpressionProcessor;

public class HAPPluginEntityProcessorComplexExpressionDataGroup extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionDataGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPExecutableEntityExpressionDataGroup.class);
	}

	@Override
	public void init(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionDataGroup executableExpresionGroup = (HAPExecutableEntityExpressionDataGroup)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionDataGroup definitionExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)defEntityInfo.getEntity();
		
		HAPUtilityExpressionProcessor.buildEntityExpressionExe(executableExpresionGroup, definitionExpressionGroup);

		//build expression in executable
		buildExpression(null, executableExpresionGroup, definitionExpressionGroup);
		
		//build constant value for expression
		processConstant(complexEntityExecutableId, processContext);
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		//resolve variable name
		resolveVariableName(complexEntityExecutableId, processContext);
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
		
		//build all variables info in expression group
		HAPUtilityExpressionProcessor.buildVariableInfo(executableExpresionGroup.getVariablesInfo(), valueStructureDomain);
		
		//process mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(complexEntityExecutableId, processContext);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresionGroup.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());

			//update value context according to variable info
			HAPUtilityExpressionProcessor.updateValueContext(executableExpresionGroup.getVariablesInfo(), valueStructureDomain);
		}
	}
	
	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

		//process referenced expression
//		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//build variable into within expression item
		HAPUtilityExpressionProcessor.buildVariableInfoInExpression(complexEntityExecutableId, processContext);
	}

	//update constant operand with constant data
	private static void processConstant(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableEntityExpressionDataGroup expressionGroupExe = (HAPExecutableEntityExpressionDataGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		for(HAPExecutableExpressionData expressionItem : expressionGroupExe.getAllExpressionItems()) {
			HAPUtilityExpressionProcessor.processConstant(expressionGroupExe, expressionItem, processContext);
		}
	}
	
	private static void resolveVariableName(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableEntityExpressionDataGroup expressionGroupExe = (HAPExecutableEntityExpressionDataGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		for(HAPExecutableExpressionData expressionItem : expressionGroupExe.getAllExpressionItems()) {
			HAPUtilityExpressionProcessor.resolveVariableName(expressionItem, expressionGroupExe.getValueContext(), expressionGroupExe.getVariablesInfo(), processContext.getCurrentValueStructureDomain());
		}
	}
	
	private void buildExpression(String expressionId, HAPExecutableEntityExpressionDataGroup expressionGroupExe, HAPDefinitionEntityExpressionDataGroup expressionGroupDef) {
		//expression element
		if(expressionId==null) {
			//all elements
			List<HAPDefinitionExpressionData> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpressionData expressionDef : expressionDefs) {
				expressionGroupExe.addExpressionItem(new HAPExecutableExpressionData(expressionDef));
			}
		}
		else {
			expressionGroupExe.addExpressionItem(new HAPExecutableExpressionData(expressionGroupDef.getEntityElement(expressionId)));
		}
	}
	
}
