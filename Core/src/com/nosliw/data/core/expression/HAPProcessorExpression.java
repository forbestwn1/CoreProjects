package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPUtilityDataAssociation;

public class HAPProcessorExpression {

	public static HAPExecutableExpressionGroup process(
			String id,
			HAPEntityWithResourceContext expressionGroupDefWithContext,
			HAPContext extraContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroup out = processBasic(id, expressionGroupDefWithContext, extraContext, expressionMan, configure, runtimeEnv, processTracker);
		
		//normalize input mapping, popup variable
		processReferencesInputMapping(out);
		
		//update reference variable name
		processReferencesVariableNameUpdate(out);

		processReferencesNameMapping(out);
		
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(configure)){
			//do discovery
			out.discover(expectOutput, runtimeEnv.getDataTypeHelper(), processTracker);
		}
		
		discoverExpressionItemVariable(out);
		
		return out;
	}

	private static void discoverExpressionItemVariable(HAPExecutableExpressionGroup expression) {
		Map<String, HAPInfoCriteria> expressionVarsInfo = expression.getVarsInfo();
		Map<String, HAPExecutableExpression> items = expression.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Map<String, HAPInfoCriteria> itemVarsInfo = new LinkedHashMap<String, HAPInfoCriteria>();
			Set<String> varNames = HAPOperandUtility.discoverVariables(item.getOperand());
			for(String varName : varNames) {
				itemVarsInfo.put(varName, expressionVarsInfo.get(varName));
			}
			item.setVariablesInfo(itemVarsInfo);
		}
	}
	
	private static HAPExecutableExpressionGroup processBasic(
			String exeId,
			HAPEntityWithResourceContext expressionGroupDefWithContext,
			HAPContext extraContext,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPDefinitionExpressionGroup expressionGroupDef = (HAPDefinitionExpressionGroup)expressionGroupDefWithContext.getEntity();
		HAPExecutableExpressionGroupInSuite out = new HAPExecutableExpressionGroupInSuite(exeId);

		//constant
		//constant --- discover constant
		Map<String, HAPData> constants = HAPUtilityExpression.getDataConstants(expressionGroupDef); 

		//variable
		//variable --- from context
		HAPContext context = HAPUtilityExpression.getContext(expressionGroupDef, extraContext, runtimeEnv);
		out.setContext(context);
		out.setVarsInfo(HAPUtilityContext.discoverDataVariablesInContext(context));

		Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
		for(HAPDefinitionExpression expressionDef : expressionDefs) {
			out.addExpression(expressionDef.getId(), new HAPOperandWrapper(expressionMan.getExpressionParser().parseExpression(expressionDef.getExpression())));
		}

		Map<String, HAPInfoCriteria> varsInfo = out.getVarsInfo();

		Map<String, HAPExecutableExpression> expressionItems = out.getExpressionItems();
		for(String id : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(id);
			HAPOperandWrapper operand = expressionItem.getOperand();
			//constant --- update constant data in expression
			HAPOperandUtility.updateConstantData(operand, constants);

			//variable --- attribute chain
			HAPOperandUtility.processAttributeOperandInExpressionOperand(operand, varsInfo);

			//variable --- discover variable
			Set<String> varNames = HAPOperandUtility.discoverVariables(operand);
			for(String varName : varNames) {
				if(varsInfo.get(varName)==null) {
					varsInfo.put(varName, HAPInfoCriteria.buildUndefinedCriteriaInfo());
				}
			}

			//referenced expression
			Map<String, HAPDefinitionReference> refDef = HAPUtilityExpression.normalizeReferenceDefinition(operand, expressionGroupDef.getEntityElement(id).getReference());
			processReferencesInOperandBasic(exeId, operand, refDef, expressionGroupDefWithContext.getResourceContext(), expressionMan, configure, runtimeEnv, processTracker);
		}
		
		return out;
	}

	//replace reference operand with referenced operand
	private static void processReferencesInOperandBasic(
			String parentId,
			HAPOperandWrapper operand, 
			Map<String, HAPDefinitionReference> referencesDef,
			HAPContextResourceDefinition resourceContext,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		int[] subId = {0};
		HAPOperandUtility.processAllOperand(operand, subId, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				int[] subId = (int[])data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					HAPDefinitionReference refDef = referencesDef.get(refName);

					referenceOperand.setElementName(refDef.getElementName());
					
					//get refered expression definition with context
					HAPEntityWithResourceContext refResourceDefWithContext = resourceContext.getResourceDefinition(refDef.getResourceId());
					
					//refered expression id
					String refExpressionId = parentId+"_"+subId[0];
					
					//process refered expression
					HAPExecutableExpressionGroup refExpressionExe = HAPProcessorExpression.processBasic(refExpressionId, refResourceDefWithContext, null, expressionMan, configure, runtimeEnv, processTracker);
					referenceOperand.setReferedExpression(refExpressionExe);
					
					//input mapping
					referenceOperand.setInputMapping(refDef.getInputMapping().cloneDataAssocation());
					
					subId[0]++;
				}
				return true;
			}
		});
	}
	
	private static void processReferencesInputMapping(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						HAPExecutableExpressionGroup refExpressionExe = referenceOperand.getReferedExpression();
						
						processReferencesInputMapping(refExpressionExe);
						
						//variable mapping
						HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
						Map<String, HAPInfoCriteria> parentVarsInfo = expressionExe.getVarsInfo();
						Map<String, HAPInfoCriteria> referedVarsInfo = referenceOperand.getReferedExpression().getVarsInfo();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPContext da = mappingDa.getAssociation();
							//refered var -- parent var
							Map<String, String> mappingPath = new LinkedHashMap<String, String>();
							for(String rootName : da.getElementNames()) {
								Map<String, String> path = HAPUtilityDataAssociation.buildRelativePathMapping(da.getElement(rootName), rootName, expressionExe.getContext());
								mappingPath.putAll(path);
							}
							for(String varName : referedVarsInfo.keySet()) {
								if(mappingPath.get(varName)==null) {
									parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneCriteriaInfo());
									HAPContextDefinitionLeafRelative relativeEle = new HAPContextDefinitionLeafRelative();
									relativeEle.setPath(varName);
									da.addElement(varName, relativeEle);
								}
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
							for(String varName : referedVarsInfo.keySet()) {
								if(parentVarsInfo.get(varName)==null) {
									parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneCriteriaInfo());
								}
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_NONE)) {
							
						}
					}
					return true;
				}
			});
		}
	}
	
	private static void processReferencesVariableNameUpdate(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			//update reference variable name
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();

						HAPExecutableExpressionGroup refExpression =	referenceOperand.getReferedExpression();
						HAPUpdateName nameUpdate = HAPUtilityExpression.getUpdateExpressionVariableName((refExpression));
						refExpression.updateVariableName(nameUpdate);
						
						HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPContext da = mappingDa.getAssociation();
							HAPContext da1 = new HAPContext();
							for(String rootName : da.getElementNames()) {
								da1.addElement(nameUpdate.getUpdatedName(rootName), da.getElement(rootName));
							}
							mappingDa.addAssociation(null, da1);
						}
						
						processReferencesVariableNameUpdate(refExpression);
					}
					return true;
				}
			});
		}
	}
	
	private static void processReferencesNameMapping(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						HAPExecutableExpressionGroup refExpressionExe = referenceOperand.getReferedExpression();
						
						Map<String, HAPInfoCriteria> parentVarsInfo = expressionExe.getVarsInfo();
						Map<String, HAPInfoCriteria> referedVarsInfo = referenceOperand.getReferedExpression().getVarsInfo();

						Map<String, String> nameMapping = new LinkedHashMap<String, String>();
						HAPDefinitionDataAssociation inputMapping =	referenceOperand.getInputMapping();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPContext da = mappingDa.getAssociation();
							for(String rootName : da.getElementNames()) {
								Map<String, String> path = HAPUtilityDataAssociation.buildRelativePathMapping(da.getElement(rootName), rootName, expressionExe.getContext());
								nameMapping.putAll(path);
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
							for(String varName : referedVarsInfo.keySet()) {
								nameMapping.put(varName, HAPUtilityExpression.getBeforeUpdateName(refExpressionExe, varName));
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_NONE)) {
							for(String varName : referedVarsInfo.keySet()) {
								parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneCriteriaInfo());
								nameMapping.put(varName, HAPUtilityExpression.getBeforeUpdateName(refExpressionExe, varName));
							}
						}
						referenceOperand.setVariableMapping(nameMapping);
						
						processReferencesNameMapping(referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
	}
}
