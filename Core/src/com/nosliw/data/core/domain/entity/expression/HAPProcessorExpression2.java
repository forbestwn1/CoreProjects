package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.complexentity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.HAPResultSolveReference;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPStructure1;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPProcessorExpression2 {

	/*
	 * id : executable id
	 * domainPool : domain env 
	 * entityId : entity for process
	 */
	public static HAPExecutableExpressionGroup process(
			String id, 
			HAPDomainEntityDefinitionLocal domainPool,
			HAPIdEntityInDomain entityId
			) {
		
		//
		
		
	}
	
	public static HAPExecutableExpressionGroup process(
			String id,
			HAPDefinitionExpressionGroup1 expressionGroupDef,
			HAPContextProcessor attachmentReferenceContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		//create execUtable 
		HAPExecutableExpressionGroup out = createExecutable(id, expressionGroupDef, null, attachmentReferenceContext, runtimeEnv, processTracker);

		//expand all reference
		expandReference(out, attachmentReferenceContext, runtimeEnv, processTracker);
		
		//constant --- update constant data in expression
		processConstant(out);
		
		//normalize variable(discovery new variable from operand or input mapping for reference)
		normalizeVariable(out);
		
		//replace name with variable id
		replaceVarNameWithId(out);
		
		//discover all variables in expression group
		buildVariableInfoInExpression(out);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(configure)){
			//do discovery
			out.discover(expectOutput, runtimeEnv.getDataTypeHelper(), processTracker);
		}
		
		//build variable into within expression item
		discoverExpressionItemVariable(out);
		
		return out;
	}
	
	private static void buildVariableInfoInExpression(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		//normalize child reference expression first
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						buildVariableInfoInExpression((HAPExecutableExpressionGroup)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
		
		HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression = new HAPContainerVariableCriteriaInfo();
		HAPContainerVariableCriteriaInfo varCrteriaInfoInStructure = HAPUtilityValueStructure.discoverDataVariablesInStructure(HAPUtilityValueStructure.getValueStructureFromWrapper(expressionExe.getValueStructureDefinitionWrapper()));
		for(String varId : HAPUtilityExpression.discoverDataVariablesIdInExpression(expressionExe)) {
			varCrteriaInfoInExpression.addVariable(varId, varCrteriaInfoInStructure.getVariableCriteriaInfo(varId));
		}
		expressionExe.setVariablesInfo(varCrteriaInfoInExpression);

	}
	
	//discover missed variable in two way
	//1. discover those variable not get mapped, add those mapping by enrich variable in parent
	//2. variable operand
	private static void normalizeVariable(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		//normalize child reference expression first
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						normalizeVariable((HAPExecutableExpressionGroup)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}

		//normalize variable mapping in reference operand first, so that all variables in referenced expression are mapped
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						
						//go through all variables in reference expression and find those that not mapped
						Map<String, HAPWrapperOperand> refExpMapping = referenceOperand.getMapping();
						Set<String> mappedVarNames = new HashSet<String>(refExpMapping.keySet());
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityExpression.discoverDataVariablesDefinitionInStructure((HAPExecutableExpressionGroup)referenceOperand.getReferedExpression());
						
						for(HAPInfoVariable varInfo : referenceExpContainer.getAllVariables()) {
							boolean mapped = false;
							for(HAPInfoAlias rootAlias : varInfo.getRootAliases()) {
								String refVarPath = new HAPComplexPath(rootAlias.getName(), varInfo.getSubPath()).getFullName();
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
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						String variablePath = variableOperand.getVariableName();
						Set<String> types = new HashSet<String>();
						types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
						HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.analyzeElementReference(variablePath, expressionExe.getValueStructureDefinitionWrapper().getValueStructure(), null, types);
						if(resolve==null || resolve.referredRoot==null || !resolve.elementInfoSolid.remainPath.isEmpty()) {
							//variable name cannot be resolved
							HAPRootStructure root = resolve==null?null:resolve.referredRoot;
							if(root==null) {
								root = HAPUtilityStructure.addRoot(expressionExe.getValueStructureDefinitionWrapper().getValueStructure(), new HAPComplexPath(variablePath).getRoot(), new HAPRootStructure());
							}
							HAPUtilityStructure.setDescendant(root, new HAPComplexPath(variablePath).getPath(), new HAPElementStructureLeafData());
						}
						
					}
					return true;
				}
			});
		}
	}
	
	//create executable 
	//in some case, executable can be created based on one expression item in group, this is case when deal with reference expression 
	private static HAPExecutableExpressionGroup createExecutable(
			String id,
			HAPDefinitionExpressionGroup1 expressionGroupDef, 
			String expressionId,
			HAPContextProcessor attachmentReferenceContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroup out = new HAPExecutableExpressionGroup(id);
		
		//structure
		HAPDefinitionWrapperValueStructure valueStructureWrapper =  expressionGroupDef.getValueStructureWrapper();
		out.setValueStructureDefinitionWrapper(valueStructureWrapper);

		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComplexConstant.getConstantsData(expressionGroupDef, HAPUtilityValueStructure.getValueStructureFromWrapper(out.getValueStructureDefinitionWrapper())));
		out.setDataConstants(constants);
		
		//expression element
		if(expressionId==null) {
			//all elements
			Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpression expressionDef : expressionDefs) {
				out.addExpression(expressionDef.getId(), new HAPWrapperOperand(runtimeEnv.getExpressionManager().getExpressionParser().parseExpression(expressionDef.getExpression())));
			}
		}
		else {
			out.addExpression(expressionId, new HAPWrapperOperand(runtimeEnv.getExpressionManager().getExpressionParser().parseExpression(expressionGroupDef.getEntityElement(expressionId).getExpression())));
		}
		
		return out;
	}
	
	//replace reference operand with referenced expression exe
	private static void expandReference(
			HAPExecutableExpressionGroup expressionGroupExe, 
			HAPContextProcessor attachmentReferenceContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		
		Map<String, HAPExecutableExpression> expressionExe = expressionGroupExe.getExpressionItems();
		for(String key : expressionExe.keySet()) {
			HAPWrapperOperand operand = expressionExe.get(key).getOperand();
			String expressionId = expressionGroupExe.getId() + "_" + key;
			int[] subId = {0};
			HAPUtilityOperand.processAllOperand(operand, subId, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
						
						HAPResultSolveReference refSolveResult = HAPUtilityComponent.solveReference(refName, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, attachmentReferenceContext);
						
//						HAPDefinitionExpressionGroup expressionGroupDefiniton = null;
//						HAPContextProcessAttachmentReference attachmentReferenceContextForRefExpression = null;
//						HAPDefinitionEntityComplex contextComplexEntity = null;
//						HAPResourceId expressionResourceId = HAPUtilityResourceId.buildResourceIdByLiterate(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, refName, true);
//						if(expressionResourceId!=null) {
//							//reference name is resource id
//							HAPResourceDefinition relatedResource = null;
//							if(attachmentReferenceContext.getComplexEntity() instanceof HAPResourceDefinition) relatedResource = (HAPResourceDefinition)attachmentReferenceContext.getComplexEntity();
//							expressionGroupDefiniton = (HAPDefinitionExpressionGroup)runtimeEnv.getResourceDefinitionManager().getResourceDefinition(expressionResourceId, relatedResource);
//							if(expressionGroupDefiniton instanceof HAPWithComplexEntity)  contextComplexEntity = ((HAPWithComplexEntity)expressionGroupDefiniton).getComplexEntity();
//							attachmentReferenceContextForRefExpression = new HAPContextProcessAttachmentReference(contextComplexEntity, runtimeEnv);
//						}
//						else {
//							//reference name is reference to attachment
//							String referenceTo = refName;
//							HAPResultProcessAttachmentReference result = attachmentReferenceContext.processReference(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, referenceTo);
//							JSONObject adaptorObj = (JSONObject)result.getAdaptor();
//							if(adaptorObj!=null && eleName==null) {
//								eleName = (String)adaptorObj.opt(HAPOperandReference.ELEMENTNAME);
//							}
//							attachmentReferenceContextForRefExpression = new HAPContextProcessAttachmentReference(result.getContextComplexEntity(), runtimeEnv);
//							expressionGroupDefiniton = (HAPDefinitionExpressionGroup)result.getEntity();
//						}
						
						if(refSolveResult.isFromAttachment()) {
							JSONObject adaptorObj = refSolveResult.getAttachmentAdapter();
							if(adaptorObj!=null && eleName==null) {
								eleName = (String)adaptorObj.opt(HAPOperandReference.ELEMENTNAME);
							}
						}
						referenceOperand.setElementName(eleName);
						
						//process refered expression
						HAPExecutableExpressionGroup refExpressionExe = HAPProcessorExpression2.createExecutable(expressionId+"_"+subId[0], (HAPDefinitionExpressionGroup1)refSolveResult.getValue(), eleName, refSolveResult.getContext(), runtimeEnv, processTracker);
						expandReference(refExpressionExe, refSolveResult.getContext(), runtimeEnv, processTracker);
						
						referenceOperand.setReferedExpression(refExpressionExe);
						
						subId[0]++;
					}
					return true;
				}
			});
		}
		
	}
	
	//update constant operand with constant data
	private static void processConstant(HAPExecutableExpressionGroup expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPUtilityOperand.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}
	
	private static void replaceVarNameWithId(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		HAPStructure1 structure = HAPUtilityValueStructure.getValueStructureFromWrapper(expressionExe.getValueStructureDefinitionWrapper());
		
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						Set<String> elementTypes = new HashSet<String>();
						elementTypes.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
						HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(variableOperand.getVariableName(), structure, null, null, elementTypes);
						variableOperand.setVariableId(new HAPComplexPath(resolve.referredRoot.getLocalId(), new HAPComplexPath(variableOperand.getVariableName()).getPath()).getFullName());
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						//replace referenced variable name mapping  
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(((HAPExecutableExpressionGroup)referenceOperand.getReferedExpression()).getValueStructureDefinitionWrapper().getValueStructure());

						Map<String, HAPWrapperOperand> mapping = referenceOperand.getMapping();
						Map<String, HAPWrapperOperand> newMapping = new LinkedHashMap<String, HAPWrapperOperand>();
						for(String refVarName : mapping.keySet()) {
							HAPInfoVariable varInfo = referenceExpContainer.getVariableInfoByAlias(refVarName);
							newMapping.put(varInfo.getIdPath().getFullName(), mapping.get(refVarName));
						}
						referenceOperand.setMapping(newMapping);
						
						//replace variable name in referenced expression
						replaceVarNameWithId((HAPExecutableExpressionGroup)referenceOperand.getReferedExpression());
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
			Set<String> varIds = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			
			HAPContainerVariableCriteriaInfo expressionVarsContainer = new HAPContainerVariableCriteriaInfo();
			for(String varId : varIds) {
				expressionVarsContainer.addVariable(varId, expressionGroupVarsContainer.getVariableCriteriaInfo(varId));
			}
			item.setVariablesInfo(expressionVarsContainer);
			
			HAPUtilityOperand.processAllOperand(item.getOperand(), name, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
