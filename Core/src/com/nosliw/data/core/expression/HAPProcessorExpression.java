package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionWithContext;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPUtilityDataAssociation;

public class HAPProcessorExpression {

	public static HAPExecutableExpression process(
			String id,
			HAPResourceDefinitionWithContext expressionDefWithContext,
			HAPContext extraContext,
			HAPDataTypeCriteria expectOutput,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {

		HAPExecutableExpression out = HAPProcessorExpression.processBasic(id, expressionDefWithContext, extraContext, expressionMan, configure, contextProcessRequirement, processTracker);
		
		//normalize input mapping, popup variable
		processReferencesInputMapping(out);
		
		//update reference variable name
		processReferencesVariableNameUpdate(out);

		processReferencesNameMapping(out);
		
		if(HAPExpressionProcessConfigureUtil.isDoDiscovery(configure)){
			//do discovery
			out.discover(expectOutput, processTracker);
		}
		
		return out;
	}
	
	private static HAPExecutableExpression processBasic(
			String id,
			HAPResourceDefinitionWithContext expressionDefWithContext,
			HAPContext extraContext,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {

		HAPDefinitionExpression expressionDef = (HAPDefinitionExpression)expressionDefWithContext.getResourceDefinition();
		HAPOperandWrapper operand = expressionDef.getOperand().cloneWrapper();

		HAPExecutableExpressionInSuite out = new HAPExecutableExpressionInSuite(expressionDef, id, operand.getOperand());

		//constant
		//constant --- discover constant
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>();
		Map<String, HAPDefinitionConstant> cstDefs = HAPDataUtility.buildConstantDefinition(expressionDef.getAttachmentContainer());
		for(String name : cstDefs.keySet()) {
			constants.put(name, cstDefs.get(name).getData());
		}
		//constant --- update constant data in expression
		HAPOperandUtility.updateConstantData(operand, constants);
		
		//variable
		//variable --- from context
		HAPContext context = (HAPContext)HAPUtilityComponent.processElementComponentContext(expressionDef, extraContext, contextProcessRequirement, HAPExpressionProcessConfigureUtil.getContextProcessConfigurationForExpression());
		out.setVarsInfo(HAPUtilityContext.discoverDataVariablesInContext(context));

		//variable --- attribute chain
		HAPOperandUtility.processAttributeOperandInExpressionOperand(operand, out.getVarsInfo());
		
		//variable --- discover variable
		Map<String, HAPVariableInfo> varsInfo = out.getVarsInfo();
		Set<String> varNames = HAPOperandUtility.discoverVariables(operand);
		for(String varName : varNames) {
			if(varsInfo.get(varName)==null) {
				varsInfo.put(varName, HAPVariableInfo.buildUndefinedVariableInfo());
			}
		}
		
		//referenced expression
		processReferencesInOperandBasic(id, operand, expressionDef.getReferenceDefinitions(), expressionDefWithContext.getResourceContext(), expressionMan, configure, contextProcessRequirement, processTracker);
		
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
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		int[] subId = {0};
		HAPOperandUtility.processAllOperand(operand, subId, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				int[] subId = (int[])data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String refName = referenceOperand.getReferenceName();
					HAPDefinitionReference refDef = referencesDef.get(refName);

					//get refered expression definition with context
					HAPResourceDefinitionWithContext refResourceDefWithContext = resourceContext.getResourceDefinition(refDef.getResourceId());
					
					//refered expression id
					String refExpressionId = parentId+"_"+subId[0];
					
					//process refered expression
					HAPExecutableExpression refExpressionExe = HAPProcessorExpression.processBasic(refExpressionId, refResourceDefWithContext, null, expressionMan, configure, contextProcessRequirement, processTracker);
					referenceOperand.setReferedExpression(refExpressionExe);
					
					//input mapping
					referenceOperand.setInputMapping(refDef.getInputMapping().cloneDataAssocation());
					
					subId[0]++;
				}
				return true;
			}
		});
	}
	
	private static void processReferencesInputMapping(HAPExecutableExpression expressionExe) {
		HAPOperandUtility.processAllOperand(expressionExe.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					HAPExecutableExpression refExpressionExe = referenceOperand.getReferedExpression();
					
					processReferencesInputMapping(refExpressionExe);
					
					//variable mapping
					HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
					Map<String, HAPVariableInfo> parentVarsInfo = expressionExe.getVarsInfo();
					Map<String, HAPVariableInfo> referedVarsInfo = referenceOperand.getReferedExpression().getVarsInfo();
					String inputMappingType = inputMapping.getType();
					if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MAPPING)) {
						HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
						HAPContext da = mappingDa.getAssociation();
						//refered var -- parent var
						Map<String, String> mappingPath = new LinkedHashMap<String, String>();
						for(String rootName : da.getElementNames()) {
							Map<String, String> path = HAPUtilityDataAssociation.buildRelativePathMapping(da.getElement(rootName), rootName, new LinkedHashMap<String, Boolean>());
							mappingPath.putAll(path);
						}
						for(String varName : referedVarsInfo.keySet()) {
							if(mappingPath.get(varName)==null) {
								parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneVariableInfo());
								HAPContextDefinitionLeafRelative relativeEle = new HAPContextDefinitionLeafRelative();
								relativeEle.setPath(varName);
								da.addElement(varName, relativeEle);
							}
						}
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MIRROR)) {
						for(String varName : referedVarsInfo.keySet()) {
							if(parentVarsInfo.get(varName)==null) {
								parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneVariableInfo());
							}
						}
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_NONE)) {
						
					}
				}
				return true;
			}
		});
	}
	
	private static void processReferencesVariableNameUpdate(HAPExecutableExpression expressionExe) {
		//update reference variable name
		HAPOperandUtility.processAllOperand(expressionExe.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					HAPExecutableExpression refExpression =	referenceOperand.getReferedExpression();
					refExpression.updateVariableName(HAPUtilityExpression.getUpdateExpressionVariableName((refExpression)));
					
					processReferencesVariableNameUpdate(refExpression);
				}
				return true;
			}
		});
	}
	
	private static void processReferencesNameMapping(HAPExecutableExpression expressionExe) {
		HAPOperandUtility.processAllOperand(expressionExe.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					HAPExecutableExpression refExpressionExe = referenceOperand.getReferedExpression();
					
					Map<String, HAPVariableInfo> parentVarsInfo = expressionExe.getVarsInfo();
					Map<String, HAPVariableInfo> referedVarsInfo = referenceOperand.getReferedExpression().getVarsInfo();

					Map<String, String> nameMapping = new LinkedHashMap<String, String>();
					HAPDefinitionDataAssociation inputMapping =	referenceOperand.getInputMapping();
					String inputMappingType = inputMapping.getType();
					if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MAPPING)) {
						HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
						HAPContext da = mappingDa.getAssociation();
						for(String rootName : da.getElementNames()) {
							Map<String, String> path = HAPUtilityDataAssociation.buildRelativePathMapping(da.getElement(rootName), rootName, new LinkedHashMap<String, Boolean>());
							nameMapping.putAll(path);
						}
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MIRROR)) {
						for(String varName : referedVarsInfo.keySet()) {
							nameMapping.put(varName, HAPUtilityExpression.getBeforeUpdateName(refExpressionExe, varName));
						}
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_NONE)) {
						for(String varName : referedVarsInfo.keySet()) {
							parentVarsInfo.put(varName, referedVarsInfo.get(varName).cloneVariableInfo());
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
