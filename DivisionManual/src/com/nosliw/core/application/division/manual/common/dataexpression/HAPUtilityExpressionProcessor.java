package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPContainerVariableCriteriaInfo;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.core.application.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandConstant;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPInfoElementResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.valueport.HAPWithInternalValuePort;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionData;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityExpressionProcessor {

	public static void resolveVariableName(HAPDataExpression expressionExe, HAPWithInternalValuePort withInternalValuePort, HAPContainerVariableCriteriaInfo varInfos, HAPConfigureResolveElementReference resolveConfigure) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();

					HAPIdElement idVariable = HAPUtilityStructureElementReference.resolveNameFromInternal(variableOperand.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure, withInternalValuePort).getElementId();
					String variableKey = varInfos.addVariable(idVariable);
					variableOperand.setVariableKey(variableKey);
					variableOperand.setVariableId(idVariable);
					expressionExe.addVariableKey(variableKey);				
				}
				return true;
			}
		});
	}

	public static void resolveReferenceVariableMapping(HAPDataExpression expressionExe, HAPRuntimeEnvironment runtimEnv) {
		HAPWrapperOperand operand = expressionExe.getOperand();
		HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
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
						HAPInfoElementResolve varInfo = HAPUtilityStructureElementReference.resolveNameFromExternal(varName, HAPConstantShared.IO_DIRECTION_IN, null, brickResourceData);
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

	public static void buildVariableInfo(HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression, HAPWithInternalValuePort withInternalValuePort) {
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
	public static void updateValuePortElements(HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression, HAPWithInternalValuePort withInternalValuePort) {
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
						HAPValuePort valuePort = HAPUtilityValuePort.getInternalValuePort(varId, withInternalValuePort);
						dataEle.setCriteria(varCriteriaInfo.getCriteria());
						valuePort.updateElement(varId, dataEle);
//						valueStructureDomain.setIsDirty(true);
					}
				}
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	public static void processConstant(HAPExecutableEntityComplex containerComplexEntity, HAPExecutableExpressionData1 expressionExe, HAPContextProcessor processContext) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPInterfaceProcessOperand(){
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
