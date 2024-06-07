package com.nosliw.data.core.domain.entity.expression.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.variable.HAPIdVariable;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.dataexpression.HAPExecutableExpressionData;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructure;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPUtilityExpressionProcessor {

	public static void resolveReferenceVariableMapping(HAPExecutableEntityExpressionData expressionGroupExe, HAPContextProcessor processContext) {
		
		List<HAPExecutableExpressionData> expressionExeItems = expressionGroupExe.getAllExpressionItems();
		
		for(HAPExecutableExpressionData expressionExeItem : expressionExeItems) {
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
							HAPIdVariable variableId = HAPUtilityValueContextReference.resolveVariableReference(
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
	
	public static void buildVariableInfo(HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression, HAPDomainValueStructure valueStructureDomain) {
		Map<HAPIdVariable, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdVariable varId : variables.keySet()) {
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
		Map<HAPIdVariable, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdVariable varId : variables.keySet()) {
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

	public static void processConstant(HAPExecutableEntityComplex containerComplexEntity, HAPExecutableExpressionData expressionExe, HAPContextProcessor processContext) {
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
	
	public static void resolveVariableName(HAPExecutableExpressionData expressionExe, HAPExecutableEntityValueContext valueContext, HAPContainerVariableCriteriaInfo varInfos, HAPDomainValueStructure valueStructureDomain) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
					HAPIdVariable idVariable = HAPUtilityValueContextReference.resolveVariableName(variableOperand.getVariableName(), valueContext, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, valueStructureDomain, null);
					String variableKey = varInfos.addVariable(idVariable);
					variableOperand.setVariableKey(variableKey);
					variableOperand.setVariableId(idVariable);
				}
				return true;
			}
		});
	}

	//build variable into within expression item
	public static void buildVariableInfoInExpression(HAPExecutableEntityExpressionData expressionGroupExe, HAPContextProcessor processContext) {
		List<HAPExecutableExpressionData> items = expressionGroupExe.getAllExpressionItems();
		for(HAPExecutableExpressionData item : items) {
			Set<String> varKeys = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			for(String varKey : varKeys) {
				item.addVariableKey(varKey);
			}
		}
	}
}
