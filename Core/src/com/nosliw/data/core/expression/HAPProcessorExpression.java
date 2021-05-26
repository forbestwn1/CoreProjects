package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.component.HAPUtilityComponentConstant;
import com.nosliw.data.core.component.HAPWithComplexEntity;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

public class HAPProcessorExpression {

	public static HAPExecutableExpressionGroup process(
			String id,
			HAPDefinitionExpressionGroup expressionGroupDef,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		//create execUtable 
		HAPExecutableExpressionGroupInSuite out = createExecutable(id, expressionGroupDef, null, attachmentReferenceContext, runtimeEnv, processTracker);

		//expand all reference
		expandReference(out, attachmentReferenceContext, runtimeEnv, processTracker);
		
		//constant --- update constant data in expression
		processConstant(out);
		
		//normalize variable(discovery new variable from operand or input mapping for reference)
		normalizeVariable(out);
		
		//replace name with variable id
		replaceVarNameWithId(out);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(configure)){
			//do discovery
			out.discover(expectOutput, runtimeEnv.getDataTypeHelper(), processTracker);
		}
		
		//build variable into within expression item
		discoverExpressionItemVariable(out);

		
		return out;
	}
	
	//discover missed variable in two way
	//1. discover those variable not get mapped, add those mapping by enrich variable in parent
	//2. variable operand
	private static void normalizeVariable(HAPExecutableExpressionGroupInSuite expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		//normalize child reference expression first
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						normalizeVariable((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}

		//normalize variable mapping in reference operand first, so that all variables in referenced expression are mapped
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						
						//go through all variables in reference expression and find those that not mapped
						Map<String, HAPOperandWrapper> refExpMapping = referenceOperand.getMapping();
						Set<String> mappedVarNames = new HashSet<String>(refExpMapping.keySet());
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression()).getStructureDefinition());
						for(HAPInfoVariable varInfo : referenceExpContainer.getAllVariables()) {
							boolean mapped = false;
							for(String rootAlias : varInfo.getRootAliases()) {
								String refVarPath = new HAPComplexPath(rootAlias, varInfo.getSubPath()).getFullName();
								if(mappedVarNames.contains(refVarPath)) {
									//variable mapped
									mapped = true;
									break;
								}
							}
							if(!mapped) {
								//variable not mapped
								String varName = new HAPComplexPath(varInfo.getPrincipleRootAlias(), varInfo.getSubPath()).getFullName();
								referenceOperand.addMapping(varName, new HAPOperandVariable(varName));
							}
						}
					}
					return true;
				}
			});
		}
		
		//normalize variable in structure
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						String variablePath = variableOperand.getVariableName();
						Set<String> types = new HashSet<String>();
						types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
						HAPInfoReferenceResolve resolve = HAPUtilityStructure.analyzeElementReference(variablePath, expressionExe.getStructureDefinition(), null, types);
						if(resolve==null || resolve.referredRoot==null || !resolve.realSolidSolved.remainPath.isEmpty()) {
							//variable name cannot be resolved
							HAPRoot root = resolve==null?null:resolve.referredRoot;
							if(root==null) {
								root = HAPUtilityStructure.addRoot(expressionExe.getStructureDefinition(), new HAPComplexPath(variablePath).getRootName(), new HAPRoot());
							}
							HAPUtilityStructure.setDescendant(root, new HAPComplexPath(variablePath).getPath(), new HAPElementLeafData());
						}
						
					}
					return true;
				}
			});
		}
		
		//discover all variables in expression group
		expressionExe.setVariablesInfo(HAPUtilityValueStructure.discoverDataVariablesInStructure(expressionExe.getStructureDefinition()));
	}
	
	//create executable 
	//in some case, executable can be created based on one expression item in group, this is case when deal with reference expression 
	private static HAPExecutableExpressionGroupInSuite createExecutable(
			String id,
			HAPDefinitionExpressionGroup expressionGroupDef, 
			String expressionId,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroupInSuite out = new HAPExecutableExpressionGroupInSuite(id);
		
		//structure
		HAPValueStructureDefinition valueStructure =  expressionGroupDef.getValueStructure();
		out.setStructureDefinition((HAPValueStructureDefinition)valueStructure.cloneStructure());

		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComponentConstant.getConstantsData(expressionGroupDef, out.getStructureDefinition()));
		out.setDataConstants(constants);
		
		//expression element
		if(expressionId==null) {
			//all elements
			Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpression expressionDef : expressionDefs) {
				out.addExpression(expressionDef.getId(), new HAPOperandWrapper(runtimeEnv.getExpressionManager().getExpressionParser().parseExpression(expressionDef.getExpression())));
			}
		}
		else {
			out.addExpression(expressionId, new HAPOperandWrapper(runtimeEnv.getExpressionManager().getExpressionParser().parseExpression(expressionGroupDef.getEntityElement(expressionId).getExpression())));
		}
		
		return out;
	}
	
	//replace reference operand with referenced expression exe
	private static void expandReference(
			HAPExecutableExpressionGroup expressionGroupExe, 
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		
		Map<String, HAPExecutableExpression> expressionExe = expressionGroupExe.getExpressionItems();
		for(String key : expressionExe.keySet()) {
			HAPOperandWrapper operand = expressionExe.get(key).getOperand();
			String expressionId = expressionGroupExe.getId() + "_" + key;
			int[] subId = {0};
			HAPOperandUtility.processAllOperand(operand, subId, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					int[] subId = (int[])data;
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						String refName = referenceOperand.getReference();
						
						String eleName = null;
						
						//try to get element name from reference name
						int eleIndex = refName.lastIndexOf("^");
						if(eleIndex!=-1) {
							eleName = refName.substring(eleIndex+1);
							refName = refName.substring(0, eleIndex);
						}
						
						HAPDefinitionExpressionGroup expressionGroupDefiniton = null;
						HAPContextProcessAttachmentReferenceExpression attachmentReferenceContextForRefExpression = null;
						HAPDefinitionEntityComplex contextComplexEntity = null;
						HAPResourceId expressionResourceId = HAPUtilityResourceId.buildResourceIdByLiterate(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, refName, true);
						if(expressionResourceId!=null) {
							//reference name is resource id
							HAPResourceDefinition relatedResource = null;
							if(attachmentReferenceContext.getComplexEntity() instanceof HAPResourceDefinition) relatedResource = (HAPResourceDefinition)attachmentReferenceContext.getComplexEntity();
							expressionGroupDefiniton = (HAPDefinitionExpressionGroup)runtimeEnv.getResourceDefinitionManager().getResourceDefinition(expressionResourceId, relatedResource);
							if(expressionGroupDefiniton instanceof HAPWithComplexEntity)  contextComplexEntity = ((HAPWithComplexEntity)expressionGroupDefiniton).getComplexEntity();
							attachmentReferenceContextForRefExpression = new HAPContextProcessAttachmentReferenceExpression(contextComplexEntity, runtimeEnv);
						}
						else {
							//reference name is reference to attachment
							String referenceTo = refName;
							HAPResultProcessAttachmentReference result = attachmentReferenceContext.processReference(referenceTo);
							JSONObject adaptorObj = (JSONObject)result.getAdaptor();
							if(adaptorObj!=null && eleName==null) {
								eleName = (String)adaptorObj.opt(HAPOperandReference.ELEMENTNAME);
							}
							attachmentReferenceContextForRefExpression = new HAPContextProcessAttachmentReferenceExpression(result.getContextComplexEntity(), runtimeEnv);
							expressionGroupDefiniton = (HAPDefinitionExpressionGroup)result.getEntity();
						}
						
						referenceOperand.setElementName(eleName);
						
						//process refered expression
						HAPExecutableExpressionGroup refExpressionExe = HAPProcessorExpression.createExecutable(expressionId+"_"+subId[0], expressionGroupDefiniton, eleName, attachmentReferenceContextForRefExpression, runtimeEnv, processTracker);
						expandReference(refExpressionExe, attachmentReferenceContextForRefExpression, runtimeEnv, processTracker);
						
						referenceOperand.setReferedExpression(refExpressionExe);
						
						subId[0]++;
					}
					return true;
				}
			});
		}
		
	}
	
	//update constant operand with constant data
	private static void processConstant(HAPExecutableExpressionGroupInSuite expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPOperandUtility.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}
	
	private static void replaceVarNameWithId(HAPExecutableExpressionGroupInSuite expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		HAPStructure structure = expressionExe.getStructureDefinition();
		
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						Set<String> elementTypes = new HashSet<String>();
						elementTypes.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
						HAPInfoReferenceResolve resolve = HAPUtilityStructure.resolveElementReference(variableOperand.getVariableName(), structure, null, elementTypes);
						variableOperand.setVariableId(new HAPComplexPath(resolve.referredRoot.getLocalId(), new HAPComplexPath(variableOperand.getVariableName()).getPath()).getFullName());
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						//replace referenced variable name mapping  
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression()).getStructureDefinition());

						Map<String, HAPOperandWrapper> mapping = referenceOperand.getMapping();
						Map<String, HAPOperandWrapper> newMapping = new LinkedHashMap<String, HAPOperandWrapper>();
						for(String refVarName : mapping.keySet()) {
							HAPInfoVariable varInfo = referenceExpContainer.getVariableInfoByAlias(refVarName);
							newMapping.put(varInfo.getIdPath().getFullName(), mapping.get(refVarName));
						}
						referenceOperand.setMapping(newMapping);
						
						//replace variable name in referenced expression
						replaceVarNameWithId((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
	}
	
	//build variable into within expression item
	private static void discoverExpressionItemVariable(HAPExecutableExpressionGroup expressionGroup) {
		HAPContainerVariableCriteriaInfo expressionGroupVarsContainer = expressionGroup.getVariablesInfo();
		Map<String, HAPExecutableExpression> items = expressionGroup.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Set<String> varNames = HAPOperandUtility.discoverVariableNames(item.getOperand());
			
			HAPContainerVariableCriteriaInfo expressionVarsContainer = new HAPContainerVariableCriteriaInfo();
			for(String varName : varNames) {
				expressionVarsContainer.addVariable(varName, expressionGroupVarsContainer.getVariableCriteriaInfo(varName));
			}
			item.setVariablesInfo(expressionVarsContainer);
			
			HAPOperandUtility.processAllOperand(item.getOperand(), name, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						discoverExpressionItemVariable(referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
	}
	
}
