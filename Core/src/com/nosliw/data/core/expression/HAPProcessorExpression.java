package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPUtilityDataAssociation;

public class HAPProcessorExpression {

	public static HAPExecutableExpressionGroup process(
			String id,
			HAPDefinitionExpressionGroup expressionGroupDef,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			HAPContextStructure extraContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroup out = processBasic(id, expressionGroupDef, attachmentReferenceContext, extraContext, expressionMan, configure, runtimeEnv, processTracker);
		
		//normalize input mapping, popup variable
		normalizeReferencesInputMapping(out);
		
		//update reference variable name
		updateReferenceVariableName(out);

		//build variable mapping (variable in referred expression -- variable in parent expression)
		buildReferencesVariableNameMapping(out);
		
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(configure)){
			//do discovery
			out.discover(expectOutput, runtimeEnv.getDataTypeHelper(), processTracker);
		}
		
		//build variable into within expression item
		discoverExpressionItemVariable(out);
		
		return out;
	}

	//build variable into within expression item
	private static void discoverExpressionItemVariable(HAPExecutableExpressionGroup expression) {
		HAPContainerVariableCriteriaInfo expressionVarsContainer = expression.getVarsInfo();
		Map<String, HAPExecutableExpression> items = expression.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Set<String> varNames = HAPOperandUtility.discoverVariables(item.getOperand());
			HAPContainerVariableCriteriaInfo itemVarsInfo = expressionVarsContainer.groupVariables(varNames);
			item.setVariablesInfo(itemVarsInfo);
		}
	}
	
	private static HAPExecutableExpressionGroup processBasic(
			String exeId,
			HAPDefinitionExpressionGroup expressionGroupDef,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			HAPContextStructure extraContext,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroupInSuite out = new HAPExecutableExpressionGroupInSuite(exeId);

		//context
		HAPContextStructure contextStructure =  expressionGroupDef.getContextStructure();
		contextStructure = HAPUtilityContext.hardMerge(contextStructure, extraContext);
		out.setContextStructure(contextStructure);

		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityExpression.getDataConstants(expressionGroupDef, out.getContextFlat()));
		
		//variable
		//variable --- from context
		out.setVarsInfo(HAPUtilityContext.discoverDataVariablesInContext(out.getContextFlat()));

		Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
		for(HAPDefinitionExpression expressionDef : expressionDefs) {
			out.addExpression(expressionDef.getId(), new HAPOperandWrapper(expressionMan.getExpressionParser().parseExpression(expressionDef.getExpression())));
		}

		HAPContainerVariableCriteriaInfo varsContainer = out.getVarsInfo();

		Set<String> unknownVarNames = new HashSet<String>();  //variables that not defined in context
		
		Map<String, HAPExecutableExpression> expressionItems = out.getExpressionItems();
		for(String id : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(id);
			HAPOperandWrapper operand = expressionItem.getOperand();
			//constant --- update constant data in expression
			HAPOperandUtility.updateConstantData(operand, constants);

			//variable --- build variable according to attribute chain
			HAPOperandUtility.processAttributeOperandInExpressionOperand(operand, varsContainer.getDataVariableNames());

			//variable --- discover variable that not defined in context
			Set<String> varNames = HAPOperandUtility.discoverVariables(operand);
			for(String varName : varNames) {
				if(varsContainer.getVariableCriteriaInfoByAlias(varName)==null) {
					unknownVarNames.add(varName);
				}
			}

			//referenced expression
			processReferencesInOperandBasic(exeId, operand, attachmentReferenceContext, expressionMan, configure, runtimeEnv, processTracker);
		}
		
		//add missed variable to variable info container
		for(String varName : unknownVarNames) {
			varsContainer.addVariableCriteriaInfo(HAPInfoCriteria.buildUndefinedCriteriaInfo(), varName);
		}
		
		return out;
	}

	//replace reference operand with referenced operand
	private static void processReferencesInOperandBasic(
			String parentId,
			HAPOperandWrapper operand, 
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
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
					
					String referenceTo = null;
					String eleName = null;
					
					String[] segs = HAPNamingConversionUtility.splitTextByComponents(refName, "AT");
					if(segs.length==1) {
						referenceTo = segs[0];
					}
					else if(segs.length == 2) {
						referenceTo = segs[0];
						eleName = segs[1];
					}

					HAPResultProcessAttachmentReference result = attachmentReferenceContext.processReference(referenceTo);
					JSONObject adaptorObj = (JSONObject)result.getAdaptor();
					if(adaptorObj!=null) {
						if(eleName==null)  eleName = (String)adaptorObj.opt(HAPOperandReference.ELEMENTNAME);
						referenceOperand.setInputMapping(HAPParserDataAssociation.buildDefinitionByJson(adaptorObj.optJSONObject(HAPDefinitionReference.INPUTMAPPING)));
					}
					referenceOperand.setElementName(eleName);
					
					//refered expression id
					String refExpressionId = parentId+"_"+subId[0];
					
					//process refered expression
					HAPExecutableExpressionGroup refExpressionExe = HAPProcessorExpression.processBasic(refExpressionId, (HAPDefinitionExpressionGroup)result.getEntity(), new HAPContextProcessAttachmentReferenceExpression(result.getContextComplexEntity(), runtimeEnv), null, expressionMan, configure, runtimeEnv, processTracker);
					referenceOperand.setReferedExpression(refExpressionExe);
					
					subId[0]++;
				}
				return true;
			}
		});
	}
	
	//discover those variable not get mapped, add those mapping by enrich variable in parent
	private static void normalizeReferencesInputMapping(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						
						normalizeReferencesInputMapping(referenceOperand.getReferedExpression());
						
						//variable mapping
						HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
						HAPContainerVariableCriteriaInfo parentVarsContainer = expressionExe.getVarsInfo();
						HAPContainerVariableCriteriaInfo referedVarsContainer = referenceOperand.getReferedExpression().getVarsInfo();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPContext da = mappingDa.getAssociation();
							//refered var -- parent var
							Map<String, String> mappingPath = new LinkedHashMap<String, String>();
							for(String rootName : da.getElementNames()) {
								Map<String, String> path = HAPUtilityDataAssociation.buildSimplifiedRelativePathMapping(da.getElement(rootName), rootName);
								mappingPath.putAll(path);
							}
							
							Set<String> missedRefVars = referedVarsContainer.findMissingVariables(mappingPath.keySet());
							for(String missedRefVar : missedRefVars) {
								//if variable in referred expression is not mapped, then pop up the variable to parent
								Set<String> aliases = referedVarsContainer.getVariableAlias(missedRefVar);
								boolean override = parentVarsContainer.addVariableCriteriaInfo(referedVarsContainer.getVariableCriteriaInfoById(missedRefVar).cloneCriteriaInfo(), aliases);
								if(override)  throw new RuntimeException();    //ref var override parent var is not allowed
								HAPContextDefinitionLeafRelative relativeEle = new HAPContextDefinitionLeafRelative();
								String varName = aliases.iterator().next();
								relativeEle.setPath(varName);
								da.addElement(varName, relativeEle);
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
							
							Set<String> missedRefVars = referedVarsContainer.findMissingVariables(parentVarsContainer.getDataVariableNames());
							for(String missedRefVar : missedRefVars) {
								//if variable in referred expression is not mapped, then pop up the variable to parent
								Set<String> aliases = referedVarsContainer.getVariableAlias(missedRefVar);
								boolean override = parentVarsContainer.addVariableCriteriaInfo(referedVarsContainer.getVariableCriteriaInfoById(missedRefVar).cloneCriteriaInfo(), aliases);
								if(override)  throw new RuntimeException();    //ref var override parent var is not allowed
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
	
	//update variable name to global name in reference in order to avoid name conflict 
	private static void updateReferenceVariableName(HAPExecutableExpressionGroup expressionExe) {
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
						HAPUpdateName nameUpdate = HAPUtilityExpression.getUpdateNameGlobal((refExpression));
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
						
						updateReferenceVariableName(refExpression);
					}
					return true;
				}
			});
		}
	}
	
	//build variable mapping (variable in referred expression -- variable in parent expression)
	private static void buildReferencesVariableNameMapping(HAPExecutableExpressionGroup expressionExe) {
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
						
						HAPContainerVariableCriteriaInfo parentContainer = expressionExe.getVarsInfo();
						HAPContainerVariableCriteriaInfo referedContainer = referenceOperand.getReferedExpression().getVarsInfo();

						Map<String, String> nameMapping = new LinkedHashMap<String, String>();
						HAPDefinitionDataAssociation inputMapping =	referenceOperand.getInputMapping();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPContext da = mappingDa.getAssociation();
							for(String rootName : da.getElementNames()) {
								Map<String, String> path = HAPUtilityDataAssociation.buildSimplifiedRelativePathMapping(da.getElement(rootName), rootName, expressionExe.getContextStructure());
								nameMapping.putAll(path);
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
							for(String varName : referedContainer.getDataVariableNames()) {
								nameMapping.put(varName, HAPUtilityExpression.getLocalName(refExpressionExe, varName));
							}
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_NONE)) {
//							for(String varName : referedContainer.keySet()) {
//								parentContainer.put(varName, referedContainer.get(varName).cloneCriteriaInfo());
//								nameMapping.put(varName, HAPUtilityExpression.getLocalName(refExpressionExe, varName));
//							}
						}
						referenceOperand.setVariableMapping(nameMapping);
						
						buildReferencesVariableNameMapping(referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
	}
}
