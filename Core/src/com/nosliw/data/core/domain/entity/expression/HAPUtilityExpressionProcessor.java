package com.nosliw.data.core.domain.entity.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPUtilityEntityProcess;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.data.HAPDefinitionEntityData;
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
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPUtilityExpressionProcessor {

	public static void buildEntityExpressionExe(HAPExecutableEntityExpression expressionEntityExe, HAPDefinitionEntityExpression expressionEntityDef) {
		expressionEntityExe.getReferenceAttributes().addAll(expressionEntityDef.getReferenceAttributes());
	}
	
	public static void processReferencedExpression(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityExpression expressionEntity = (HAPDefinitionEntityExpression)currentBundle.getDefinitionDomain().getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		for(String refAtt : expressionEntity.getReferenceAttributes()) {
			HAPUtilityEntityProcess.processComplexAttribute(refAtt, complexEntityExecutableId, processContext);
		}
	}
	
	public static void resolveReferenceVariableMapping(HAPIdEntityInDomain expreesionEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableEntityExpression expressionGroupExe = (HAPExecutableEntityExpression)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionEntityIdExe).getEntity();
		
		List<HAPExecutableExpression> expressionExeItems = expressionGroupExe.getAllExpressionItems();
		
		for(HAPExecutableExpression expressionExeItem : expressionExeItems) {
			HAPWrapperOperand operand = expressionExeItem.getOperand();
			HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						Map<String, HAPWrapperOperand> referenceMapping = referenceOperand.getMapping();
						
						String refAttributeName = referenceOperand.getReferenceExpressionAttributeName();
						HAPIdEntityInDomain referedExperssionEntityId = (HAPIdEntityInDomain)expressionGroupExe.getAttributeValue(refAttributeName);
						Pair<HAPExecutableEntity, HAPContextProcessor> referencedEntityInfo = HAPUtilityDomain.resolveExecutableEntityId(referedExperssionEntityId, processContext);
						HAPDomainValueStructure valueStructureDomain = referencedEntityInfo.getRight().getCurrentValueStructureDomain();
						
						HAPExecutableEntityExpressionSingle referedExpressionExe = (HAPExecutableEntityExpressionSingle)referencedEntityInfo.getRight().getCurrentExecutableDomain().getEntityInfoExecutable(referedExperssionEntityId).getEntity();
						referenceOperand.setOutputCriteria(referedExpressionExe.getExpression().getOutputCriteria());
						
						for(String varName : referenceMapping.keySet()) {
							HAPIdVariable variableId = HAPUtilityValueContextReference.resolveVariableReference(
									new HAPReferenceElementInValueContext(varName), 
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
	
	public static void processConstant(HAPExecutableEntityComplex containerComplexEntity, HAPExecutableExpression expressionExe, HAPContextProcessor processContext) {
		HAPUtilityOperand.processAllOperand(expressionExe.getOperand(), null, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null) {
						String constantName = constantOperand.getName();
						HAPAttachment attachment = processContext.getCurrentBundle().getAttachmentDomain().getAttachment(containerComplexEntity.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATA, constantName);
						HAPDefinitionEntityData dataEntity = (HAPDefinitionEntityData)processContext.getCurrentDefinitionDomain().getEntityInfoDefinition(attachment.getEntityId()).getEntity();
						constantOperand.setData(dataEntity.getData());
					}
				}
				return true;
			}
		});
	}
	

	public static void resolveVariableName(HAPExecutableExpression expressionExe, HAPExecutableEntityValueContext valueContext, HAPContainerVariableCriteriaInfo varInfos, HAPDomainValueStructure valueStructureDomain) {
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
				else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
/*						
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					//replace referenced variable name mapping
					
//					Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroup, HAPComplexValueStructure> refEntityInfo = getComplexEntityByExecutableId(referenceOperand.getReferedExpression(), processContext);
					HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(refEntityInfo.getMiddle().getValueContextEntity(), processContext.getDomainContext().getValueStructureDomain());

					Map<String, HAPWrapperOperand> mapping = referenceOperand.getMapping();
					Map<String, HAPWrapperOperand> newMapping = new LinkedHashMap<String, HAPWrapperOperand>();
					for(String refVarName : mapping.keySet()) {
						HAPInfoVariable varInfo = referenceExpContainer.getVariableInfoByAlias(refVarName);
						newMapping.put(varInfo.getIdPath().getFullName(), mapping.get(refVarName));
					}
					referenceOperand.setMapping(newMapping);
					
					//replace variable name in referenced expression
//					replaceVarNameWithId((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());

*/
				}
				return true;
			}
		});
	}

	//build variable into within expression item
	public static void buildVariableInfoInExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableEntityExpression expressionGroupExe = (HAPExecutableEntityExpression)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		List<HAPExecutableExpression> items = expressionGroupExe.getAllExpressionItems();
		for(HAPExecutableExpression item : items) {
			Set<String> varKeys = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			for(String varKey : varKeys) {
				item.addVariableKey(varKey);
			}
		}
	}
}