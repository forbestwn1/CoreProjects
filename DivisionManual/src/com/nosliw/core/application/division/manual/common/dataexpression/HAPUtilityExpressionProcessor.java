package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataexpression.HAPContainerVariableCriteriaInfo;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.core.application.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandConstant;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPWithInternalValuePort;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionData;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionDataSingle;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructure;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPUtilityExpressionProcessor {

	public static void resolveVariableName(HAPExecutableExpressionData expressionExe, HAPWithInternalValuePort withInternalBrick, HAPContainerVariableCriteriaInfo varInfos) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
					
					HAPReferenceElement ref = new HAPReferenceElement();
					ref.buildObject(variableOperand.getVariableName(), HAPSerializationFormat.JSON);
					ref.setValuePortId(HAPUtilityValuePort.normalizeInternalValuePortId(ref.getValuePortId(), HAPConstantShared.IO_DIRECTION_OUT, withInternalBrick));

					HAPIdElement idVariable = HAPUtilityStructureElementReference.resolveElementReferenceInternal(ref, null, withInternalBrick);
					String variableKey = varInfos.addVariable(idVariable);
					variableOperand.setVariableKey(variableKey);
					variableOperand.setVariableId(idVariable);
					expressionExe.addVariableKey(variableKey);				}
				return true;
			}
		});
	}

	public static void resolveReferenceVariableMapping(HAPExecutableEntityExpressionData expressionGroupExe, HAPContextProcessor processContext) {
		
		List<HAPExecutableExpressionData1> expressionExeItems = expressionGroupExe.getAllExpressionItems();
		
		for(HAPExecutableExpressionData1 expressionExeItem : expressionExeItems) {
			HAPWrapperOperand operand = expressionExeItem.getOperand();
			HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						Map<String, HAPWrapperOperand> referenceMapping = referenceOperand.getMapping();
						
						String refAttributeName = referenceOperand.getReferenceExpressionAttributeName();
						
						Pair<HAPExecutable, HAPContextProcessor> referencedEntityInfo = HAPUtilityDomain.resolveAttributeExecutableEntity(expressionGroupExe.getReferences(), refAttributeName, processContext);
						HAPDomainValueStructure valueStructureDomain = referencedEntityInfo.getRight().getCurrentValueStructureDomain();
						
						HAPExecutableEntityExpressionDataSingle referedExpressionExe = (HAPExecutableEntityExpressionDataSingle)referencedEntityInfo.getLeft();
						referenceOperand.setOutputCriteria(referedExpressionExe.getExpression().getOutputCriteria());
						
						for(String varName : referenceMapping.keySet()) {
							HAPIdElement variableId = HAPUtilityValueContextReference.resolveVariableReference(
									new HAPReferenceElement(varName), 
									Sets.newHashSet(HAPUtilityValueStructure.getVisibleToExternalCategaries()), 
									((HAPExecutableEntityComplex)referencedEntityInfo.getLeft()).getValueContext(), 
									valueStructureDomain, 
									null);
							HAPElementStructureLeafData structureEle = (HAPElementStructureLeafData)HAPUtilityValueContext.getStructureElement(variableId, valueStructureDomain);
							referenceOperand.addResolvedMappingTo(varName, variableId, structureEle);
						}
					}
					return true;
				}
			});
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
	
	
	public static void buildVariableInfo(HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression, HAPDomainValueStructure valueStructureDomain) {
		Map<HAPIdElement, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdElement varId : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = variables.get(varId);
			HAPElementStructure structureEle = HAPUtilityValueContext.getStructureElement(varId, valueStructureDomain);
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				varCriteriaInfo.setCriteria(dataEle.getCriteria());
				varCriteriaInfo.setStatus(dataEle.getStatus());
			}
		}
	}

	//update value context according to vairable info
	public static void updateValueContext(HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression, HAPDomainValueStructure valueStructureDomain) {
		Map<HAPIdElement, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdElement varId : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = variables.get(varId);
			HAPElementStructure structureEle = HAPUtilityValueContext.getStructureElement(varId, valueStructureDomain);
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				if(dataEle.getStatus().equals(HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN)) {
					if(!HAPUtilityBasic.isEquals(dataEle.getCriteria(), varCriteriaInfo.getCriteria())){
						dataEle.setCriteria(varCriteriaInfo.getCriteria());
						valueStructureDomain.setIsDirty(true);
					}
				}
			}
		}
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
