package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.complex.HAPResultSolveReference;
import com.nosliw.data.core.complex.HAPUtilityComplexConstant;
import com.nosliw.data.core.complex.HAPUtilityComplexEntity;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomain;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

public class HAPPluginProcessorExpression implements HAPPluginEntityDefinitionInDomain{

	/*
	 * id : executable id
	 * domainPool : domain env 
	 * entityId : entity for process
	 * return entity id for exe
	 */
	@Override
	public HAPIdEntityInDomain processDefinition(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		
		//go through all complex entity in definition domain, create executable in executable domain
		
		//go though all complex entity, process all attachment, put attachments in executable domain
		
		//go through all complex entity, process all value structure complex, put them in executable domain
		
		//go through all complex entity, discover all implied value structure
		
		//      
		
		
		//when a new complex entity definition added
		//      
		
		
		//
		
		
		
		//expand value structure
		
		//
		
		
		HAPIdEntityInDomain out = HAPUtilityComplexEntity.newExecutableEntity(HAPExecutableExpressionGroupInSuite.class, complexEntityDefinitionId, processContext);

		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> entityInfo = getComplexEntityByExecutableId(out, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = entityInfo.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = entityInfo.getLeft();

		//build expression in executable
		buildExpression(out, null, processContext);
		
		//build constant value for expression
		buildConstant(out, processContext);
		
		//expand reference 
		expandReference(out, processContext);

		//replace variable name with id
		replaceVarNameWithId(out, processContext);
		
		//discover all variables in expression group
		buildVariableInfoInExpression(out, processContext);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(expressionGroupDef.getInfo())){
			//do discovery
			expressionGroupExe.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		}
		
		//build variable into within expression item
		discoverExpressionItemVariable(out, processContext);

		return out;
	}

	//build variable into within expression item
	private static void discoverExpressionItemVariable(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> entityInfo = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = entityInfo.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = entityInfo.getLeft();

		
		HAPContainerVariableCriteriaInfo expressionGroupVarsContainer = expressionGroupExe.getVariablesInfo();
		Map<String, HAPExecutableExpression> items = expressionGroupExe.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Set<String> varIds = HAPOperandUtility.discoverVariableIds(item.getOperand());
			
			HAPContainerVariableCriteriaInfo expressionVarsContainer = new HAPContainerVariableCriteriaInfo();
			for(String varId : varIds) {
				expressionVarsContainer.addVariable(varId, expressionGroupVarsContainer.getVariableCriteriaInfo(varId));
			}
			item.setVariablesInfo(expressionVarsContainer);
		}
	}
	

	private static void buildVariableInfoInExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> entityInfo = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = entityInfo.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = entityInfo.getLeft();

		HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression = new HAPContainerVariableCriteriaInfo();
		
		HAPContainerVariableCriteriaInfo varCrteriaInfoInStructure = HAPUtilityValueStructure.discoverDataVariablesInStructure(expressionGroupDef.getValueStructureComplexId(), processContext.getDomainContext().getValueStructureDomain());
		for(String varId : HAPUtilityExpression.discoverDataVariablesIdInExpression(expressionGroupExe)) {
			varCrteriaInfoInExpression.addVariable(varId, varCrteriaInfoInStructure.getVariableCriteriaInfo(varId));
		}
		expressionGroupExe.setVariablesInfo(varCrteriaInfoInExpression);
	}
	

	private static void replaceVarNameWithId(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> entityInfo = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = entityInfo.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = entityInfo.getLeft();
		HAPComplexValueStructure valueStructureComplex = entityInfo.getRight();

		Map<String, HAPExecutableExpression> expressionItems = expressionGroupExe.getExpressionItems();
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
						HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(variableOperand.getVariableName(), valueStructureComplex, processContext.getDomainContext().getValueStructureDomain(), null, elementTypes); 
						variableOperand.setVariableId(new HAPComplexPath(resolve.referredRoot.getLocalId(), new HAPComplexPath(variableOperand.getVariableName()).getPath()).getFullName());
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						//replace referenced variable name mapping
						
						Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> refEntityInfo = getComplexEntityByExecutableId(referenceOperand.getReferedExpression(), processContext);
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(refEntityInfo.getMiddle().getValueStructureComplexId(), processContext.getDomainContext().getValueStructureDomain());

						Map<String, HAPOperandWrapper> mapping = referenceOperand.getMapping();
						Map<String, HAPOperandWrapper> newMapping = new LinkedHashMap<String, HAPOperandWrapper>();
						for(String refVarName : mapping.keySet()) {
							HAPInfoVariable varInfo = referenceExpContainer.getVariableInfoByAlias(refVarName);
							newMapping.put(varInfo.getIdPath().getFullName(), mapping.get(refVarName));
						}
						referenceOperand.setMapping(newMapping);
						
						//replace variable name in referenced expression
//						replaceVarNameWithId((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}
	}

	
	//replace reference operand with referenced expression exe
	private void expandReference(
			HAPIdEntityInDomain expreesionGroupEntityIdExe,
			HAPContextProcessor processContext) {
		
		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> triple = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = triple.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = triple.getLeft();

		Map<String, HAPExecutableExpression> expressionExe = expressionGroupExe.getExpressionItems();
		for(String key : expressionExe.keySet()) {
			HAPOperandWrapper operand = expressionExe.get(key).getOperand();
			HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
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
						
						HAPResultSolveReference refSolveResult = HAPUtilityComplexEntity.solveReference(refName, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, processContext);
						
						if(refSolveResult.isFromAttachment()) {
							JSONObject adaptorObj = refSolveResult.getAttachmentAdapter();
							if(adaptorObj!=null && eleName==null) {
								eleName = (String)adaptorObj.opt(HAPOperandReference.ELEMENTNAME);
							}
						}
						referenceOperand.setElementName(eleName);
						
						//process refered expression
						HAPIdEntityInDomain refExpreesionGroupEntityIdExe = processContext.getRuntimeEnvironment().getComplexEntityManager().process(refSolveResult.getEntityId(), processContext);
						referenceOperand.setReferedExpression(refExpreesionGroupEntityIdExe);
					}
					return true;
				}
			});
		}
		
	}
	
	private void buildExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, String expressionId, HAPContextProcessor processContext) {

		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> triple = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = triple.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = triple.getLeft();
		
		//expression element
		if(expressionId==null) {
			//all elements
			Set<HAPDefinitionExpression> expressionDefs = triple.getLeft().getEntityElements();
			for(HAPDefinitionExpression expressionDef : expressionDefs) {
				expressionGroupExe.addExpression(expressionDef.getId(), new HAPOperandWrapper(processContext.getRuntimeEnvironment().getExpressionManager().getExpressionParser().parseExpression(expressionDef.getExpression())));
			}
		}
		else {
			expressionGroupExe.addExpression(expressionId, new HAPOperandWrapper(processContext.getRuntimeEnvironment().getExpressionManager().getExpressionParser().parseExpression(expressionGroupDef.getEntityElement(expressionId).getExpression())));
		}
	}
	
	//create executable 
	//in some case, executable can be created based on one expression item in group, this is case when deal with reference expression 
	private void buildConstant(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {

		Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> triple = getComplexEntityByExecutableId(expreesionGroupEntityIdExe, processContext);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = triple.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = triple.getLeft();
		
		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComplexConstant.getConstantsData(expressionGroupDef, processContext.getDomainContext().getValueStructureDomain()));
		expressionGroupExe.setDataConstants(constants);
	}
	

	private static Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroupInSuite, HAPComplexValueStructure> getComplexEntityByExecutableId(HAPIdEntityInDomain executableId, HAPContextProcessor processContext){
		Triple<HAPDefinitionEntityComplex, HAPExecutableEntityComplex, HAPComplexValueStructure> triple = processContext.getDomainContext().getComplexEntityInfoByExecutableId(executableId);
		HAPExecutableExpressionGroupInSuite expressionGroupExe = (HAPExecutableExpressionGroupInSuite)triple.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = (HAPDefinitionExpressionGroup1)triple.getLeft();
		return Triple.of(expressionGroupDef, expressionGroupExe, triple.getRight());
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
		HAPExecutableExpressionGroupInSuite out = createExecutable(id, expressionGroupDef, null, attachmentReferenceContext, runtimeEnv, processTracker);

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
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityExpression.discoverDataVariablesDefinitionInStructure((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());
						
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
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						String variablePath = variableOperand.getVariableName();
						Set<String> types = new HashSet<String>();
						types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
						HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.analyzeElementReference(variablePath, expressionExe.getValueStructureDefinitionWrapper().getValueStructure(), null, types);
						if(resolve==null || resolve.referredRoot==null || !resolve.realSolidSolved.remainPath.isEmpty()) {
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
	
	//update constant operand with constant data
	private static void processConstant(HAPExecutableExpressionGroupInSuite expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPOperandUtility.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}

	@Override
	public void enhanceValueStructure(HAPIdEntityInDomain complexEntityDefinitionId,
			HAPContextProcessor processContext) {
		// TODO Auto-generated method stub
		
	}
	
	
}
