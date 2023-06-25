package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityComplex;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.complexentity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.complexentity.HAPUtilityComplexEntity;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.HAPResultSolveReference;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.data.HAPDefinitionEntityData;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPPluginEntityProcessorComplexExpressionGroup extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionGroup() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPExecutableExpressionGroup.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {

		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableExpressionGroup executableExpresionGroup = (HAPExecutableExpressionGroup)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueStructureComplex = executableExpresionGroup.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionGroup definitionExpressionGroup = (HAPDefinitionEntityExpressionGroup)defEntityInfo.getEntity();

		//build expression in executable
		buildExpression(null, executableExpresionGroup, definitionExpressionGroup, processContext.getRuntimeEnvironment().getExpressionManager().getExpressionParser());
		
		//build constant value for expression
		processConstant(complexEntityExecutableId, processContext);
		
		//expand reference 
//		expandReference(exeEntityId, processContext);

		//replace variable name with id
		resolveVariableName(complexEntityExecutableId, processContext);
		
		//build all variables info in expression group
		buildVariableInfo(complexEntityExecutableId, processContext);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresionGroup.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		}
		
		//build variable into within expression item
		buildVariableInfoInExpression(complexEntityExecutableId, processContext);

	}

	//update constant operand with constant data
	private static void processConstant(HAPExecutableExpressionGroup expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPUtilityOperand.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}

	private static void processConstant(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		for(HAPExecutableExpression expressionItem : expressionGroupExe.getExpressionItems()) {
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
						HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
						if(constantOperand.getData()==null) {
							String constantName = constantOperand.getName();
							HAPAttachment attachment = processContext.getCurrentBundle().getAttachmentDomain().getAttachment(expressionGroupExe.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATA, constantName);
							HAPDefinitionEntityData dataEntity = (HAPDefinitionEntityData)processContext.getCurrentDefinitionDomain().getEntityInfoDefinition(attachment.getEntityId()).getEntity();
							constantOperand.setData(dataEntity.getData());
						}
					}
					return true;
				}
			});
		}
	}

	
	//build variable into within expression item
	private static void buildVariableInfoInExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		HAPContainerVariableCriteriaInfo expressionGroupVarsContainer = expressionGroupExe.getVariablesInfo();
		List<HAPExecutableExpression> items = expressionGroupExe.getExpressionItems();
		for(HAPExecutableExpression item : items) {
			Set<String> varKeys = HAPUtilityOperand.discoverVariableKeys(item.getOperand());
			
			HAPContainerVariableCriteriaInfo expressionVarsContainer = new HAPContainerVariableCriteriaInfo();
			for(String varKey : varKeys) {
				expressionVarsContainer.addVariable(varKey, expressionGroupVarsContainer.getVariableId(varKey), expressionGroupVarsContainer.getVaraibleCriteriaInfo(varKey));
			}
			item.setVariablesInfo(expressionVarsContainer);
		}
	}
	
	private static void buildVariableInfo(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression = expressionGroupExe.getVariablesInfo();
		Map<HAPIdVariable, HAPInfoCriteria> variables = varCrteriaInfoInExpression.getVariableCriteriaInfos();
		for(HAPIdVariable varId : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = variables.get(varId);
			HAPElementStructure structureEle = HAPUtilityValueContext.getStructureElement(varId, processContext.getCurrentValueStructureDomain());
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				varCriteriaInfo.setCriteria(dataEle.getCriteria());
				varCriteriaInfo.setStatus(dataEle.getStatus());
			}
		}
	}
	
	private static void resolveVariableName(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(expreesionGroupEntityIdExe).getEntity();
		
		for(HAPExecutableExpression expressionItem : expressionGroupExe.getExpressionItems()) {
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						HAPIdVariable idVariable = HAPUtilityValueContextReference.resolveVariableName(variableOperand.getVariableName(), expreesionGroupEntityIdExe, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, processContext, null);
						String variableKey = expressionGroupExe.getVariablesInfo().addVariable(idVariable);
						variableOperand.setVariableKey(variableKey);
						variableOperand.setVariableId(idVariable);
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
/*						
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						//replace referenced variable name mapping
						
//						Triple<HAPDefinitionExpressionGroup1, HAPExecutableExpressionGroup, HAPComplexValueStructure> refEntityInfo = getComplexEntityByExecutableId(referenceOperand.getReferedExpression(), processContext);
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(refEntityInfo.getMiddle().getValueContextEntity(), processContext.getDomainContext().getValueStructureDomain());

						Map<String, HAPWrapperOperand> mapping = referenceOperand.getMapping();
						Map<String, HAPWrapperOperand> newMapping = new LinkedHashMap<String, HAPWrapperOperand>();
						for(String refVarName : mapping.keySet()) {
							HAPInfoVariable varInfo = referenceExpContainer.getVariableInfoByAlias(refVarName);
							newMapping.put(varInfo.getIdPath().getFullName(), mapping.get(refVarName));
						}
						referenceOperand.setMapping(newMapping);
						
						//replace variable name in referenced expression
//						replaceVarNameWithId((HAPExecutableExpressionGroupInSuite)referenceOperand.getReferedExpression());
 
 */
					}
					return true;
				}
			});
		}
	}


	
	private void buildExpression(String expressionId, HAPExecutableExpressionGroup expressionGroupExe, HAPDefinitionEntityExpressionGroup expressionGroupDef, HAPParserExpression expressionParser) {
		//expression element
		if(expressionId==null) {
			//all elements
			List<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpression expressionDef : expressionDefs) {
				expressionGroupExe.addExpressionItem(new HAPExecutableExpression(expressionDef, expressionParser));
			}
		}
		else {
			expressionGroupExe.addExpressionItem(new HAPExecutableExpression(expressionGroupDef.getEntityElement(expressionId), expressionParser));
		}
	}
	
	//create executable 
	//in some case, executable can be created based on one expression item in group, this is case when deal with reference expression 
	private void buildConstant(HAPExecutableExpressionGroup expressionGroupExe, HAPDefinitionEntityExpressionGroup expressionGroupDef, HAPDomainValueStructure valueStructureDomain) {
		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComplexConstant.getConstantsData(expressionGroupDef, valueStructureDomain));
		expressionGroupExe.setDataConstants(constants);
	}
	
	//replace reference operand with referenced expression exe
	private void expandReference(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPInfoEntityComplex complexEntityInfo = processContext.getDomainContext().getComplexEntityInfoByExecutableId(expreesionGroupEntityIdExe);
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)complexEntityInfo.getExecutable();
		HAPDefinitionEntityExpressionGroup expressionGroupDef = (HAPDefinitionEntityExpressionGroup)complexEntityInfo.getDefinition();

		Map<String, HAPExecutableExpression> expressionExe = expressionGroupExe.getExpressionItems();
		for(String key : expressionExe.keySet()) {
			HAPWrapperOperand operand = expressionExe.get(key).getOperand();
			HAPUtilityOperand.processAllOperand(operand, null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
						HAPIdEntityInDomain refExpreesionGroupEntityIdExe = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().process(refSolveResult.getEntityId(), processContext);
						referenceOperand.setReferedExpression(refExpreesionGroupEntityIdExe);
					}
					return true;
				}
			});
		}
	}

	
	private static Triple<HAPDefinitionEntityExpressionGroup, HAPExecutableExpressionGroup, HAPDefinitionEntityValueContext> getComplexEntityByExecutableId(HAPIdEntityInDomain executableId, HAPContextProcessor processContext){
		Triple<HAPDefinitionEntityInDomainComplex, HAPExecutableEntityComplex, HAPDefinitionEntityValueContext> triple = processContext.getDomainContext().getComplexEntityInfoByExecutableId(executableId);
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)triple.getMiddle();
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
	
	//update constant operand with constant data
	private static void processConstant1(HAPExecutableExpressionGroup expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPUtilityOperand.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}

	
}
