package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandConstant;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression1.HAPWrapperOperand;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPInfoElementResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort1111;
import com.nosliw.core.application.common.valueport.HAPWithInternalValuePort;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionData;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityExpressionProcessor {

	public static Pair<HAPContainerVariableInfo, List<HAPMatchers>> processDataExpressionGroup(HAPContainerDataExpression dataExpressionGroup, List<HAPDataTypeCriteria> expectOutput, HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort, HAPRuntimeEnvironment runtimeEnv) {
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		HAPContainerVariableInfo currentVarInfoContainer = varInfoContainer;
		for(int i=0; i<dataExpressionGroup.getItems().size(); i++) {
			HAPItemInContainerDataExpression item = dataExpressionGroup.getItems().get(i); 
			Pair<HAPContainerVariableInfo, HAPMatchers> pair = processDataExpression(item.getDataExpression(), expectOutput.get(i), varInfoContainer, withInternalValuePort, runtimeEnv);
			currentVarInfoContainer = pair.getLeft();
			matchers.add(pair.getRight());
		}
		return Pair.of(currentVarInfoContainer, matchers);
	}
	
	public static Pair<HAPContainerVariableInfo, HAPMatchers> processDataExpression(HAPExpressionData dataExpression, HAPDataTypeCriteria expectOutput, HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort, HAPRuntimeEnvironment runtimeEnv) {
		
		//resolve variable name, build var info container
		HAPUtilityExpressionProcessor.resolveVariableName(dataExpression, withInternalValuePort, varInfoContainer, null);
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityExpressionProcessor.buildVariableInfo(varInfoContainer, withInternalValuePort);
		
		//process reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(dataExpression, runtimeEnv);
		
		//discover
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		operands.add(dataExpression.getOperand().getOperand());
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		expectOutputs.add(expectOutput);
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		HAPContainerVariableInfo variableInfos = HAPUtilityOperand.discover(
				operands,
				expectOutputs,
				varInfoContainer,
				matchers,
				runtimeEnv.getDataTypeHelper(),
				new HAPProcessTracker());
		
		//update value port element according to var info container after resolve
		HAPUtilityExpressionProcessor.updateValuePortElements(varInfoContainer, withInternalValuePort);

		return Pair.of(variableInfos, matchers.get(0));
	}
	

	
	public static void resolveVariableName(HAPExpressionData expressionExe, HAPWithInternalValuePort withInternalValuePort, HAPContainerVariableInfo varInfos, HAPConfigureResolveElementReference resolveConfigure) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPManualHandlerOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();

					HAPIdElement idVariable = HAPUtilityStructureElementReference.resolveNameFromInternal(variableOperand.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, withInternalValuePort, resolveConfigure).getElementId();
					String variableKey = varInfos.addVariable(idVariable);
					variableOperand.setVariableKey(variableKey);
					variableOperand.setVariableId(idVariable);
					expressionExe.addVariableKey(variableKey);				
				}
				return true;
			}
		});
	}

	public static void resolveReferenceVariableMapping(HAPExpressionData expressionExe, HAPRuntimeEnvironment runtimEnv) {
		HAPWrapperOperand operand = expressionExe.getOperand();
		HAPUtilityOperand.processAllOperand(operand, null, new HAPManualHandlerOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					
					HAPResourceId refResourceId = HAPFactoryResourceId.newInstance(referenceOperand.getReference());
					referenceOperand.setResourceId(refResourceId);
					HAPBlockDataExpressionElementInLibrary brickResourceData = (HAPBlockDataExpressionElementInLibrary)HAPUtilityResource.getResourceDataBrick(refResourceId, runtimEnv.getResourceManager(), runtimEnv.getRuntime().getRuntimeInfo());
					
					Map<String, HAPWrapperOperand> referenceMapping = referenceOperand.getMapping();
					for(String varName : referenceMapping.keySet()) {
						HAPInfoElementResolve varInfo = HAPUtilityStructureElementReference.resolveNameFromExternal(varName, HAPConstantShared.IO_DIRECTION_IN, brickResourceData, null);
						HAPElementStructure eleStructure = varInfo.getElementStructure();
						String eleType = eleStructure.getType();
						if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
							HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)eleStructure;
							referenceOperand.addResolvedVariable(varName, varInfo.getElementId(), dataEle.getCriteria());
						} else {
							throw new RuntimeException();
						}
					}
				}
				return true;
			}
		});
	}

	public static void buildVariableInfo(HAPContainerVariableInfo varCrteriaInfoInExpression, HAPWithInternalValuePort withInternalValuePort) {
		Map<HAPIdElement, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdElement varId : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = variables.get(varId);
			
			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(varId, withInternalValuePort); 
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				varCriteriaInfo.setCriteria(dataEle.getCriteria());
				varCriteriaInfo.setStatus(dataEle.getStatus());
			}
		}
	}
	
	//update value context according to vairable info
	public static void updateValuePortElements(HAPContainerVariableInfo varCrteriaInfoInExpression, HAPWithInternalValuePort withInternalValuePort) {
		Map<HAPIdElement, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdElement varId : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = variables.get(varId);

			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(varId, withInternalValuePort);
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
//				if(dataEle.getStatus().equals(HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN)) 
				{
					if(!HAPUtilityBasic.isEquals(dataEle.getCriteria(), varCriteriaInfo.getCriteria())){
						HAPValuePort1111 valuePort = HAPUtilityValuePort.getInternalValuePort(varId, withInternalValuePort);
						dataEle.setCriteria(varCriteriaInfo.getCriteria());
						valuePort.updateElement(varId, dataEle);
//						valueStructureDomain.setIsDirty(true);
					}
				}
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	public static void processConstant(HAPExecutableEntityComplex containerComplexEntity, HAPExecutableExpressionData1 expressionExe, HAPContextProcessor processContext) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPManualHandlerOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null) {
						HAPData constantData = containerComplexEntity.getConstantData(constantOperand.getName());
						constantOperand.setData(constantData);
					}
				}
				return true;
			}
		});
	}
	
	
	//build variable into within expression item
	public static void buildVariableInfoInExpression(HAPExecutableEntityExpressionData expressionGroupExe, HAPContextProcessor processContext) {
		List<HAPExecutableExpressionData1> items = expressionGroupExe.getAllExpressionItems();
		for(HAPExecutableExpressionData1 item : items) {
			Set<String> varKeys = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			for(String varKey : varKeys) {
				item.addVariableKey(varKey);
			}
		}
	}
}
