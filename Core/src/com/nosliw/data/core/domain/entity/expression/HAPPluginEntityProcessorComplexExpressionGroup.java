package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.complex.HAPResultSolveReference;
import com.nosliw.data.core.complex.HAPUtilityComplexConstant;
import com.nosliw.data.core.complex.HAPUtilityComplexEntity;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

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
		HAPDefinitionEntityExpressionGroup definitionExpressionGroup = (HAPDefinitionEntityExpressionGroup)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		
		//build expression in executable
		buildExpression(null, executableExpresionGroup, definitionExpressionGroup, processContext.getRuntimeEnvironment().getExpressionManager().getExpressionParser());
		
		//build constant value for expression
		buildConstant(executableExpresionGroup, definitionExpressionGroup, valueStructureDomain);
		
		//expand reference 
		expandReference(exeEntityId, processContext);

		//replace variable name with id
		replaceVarNameWithId(exeEntityId, processContext);
		
		//discover all variables in expression group
		buildVariableInfoInExpression(exeEntityId, processContext);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(expressionGroupDef.getInfo())){
			//do discovery
			expressionGroupExe.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());
		}
		
		//build variable into within expression item
		discoverExpressionItemVariable(out, processContext);

	}

	private void buildExpression(String expressionId, HAPExecutableExpressionGroup expressionGroupExe, HAPDefinitionEntityExpressionGroup expressionGroupDef, HAPParserExpression expressionParser) {
		//expression element
		if(expressionId==null) {
			//all elements
			Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpression expressionDef : expressionDefs) {
				expressionGroupExe.addExpression(expressionDef.getId(), new HAPWrapperOperand(expressionParser.parseExpression(expressionDef.getExpression())));
			}
		}
		else {
			expressionGroupExe.addExpression(expressionId, new HAPWrapperOperand(expressionParser.parseExpression(expressionGroupDef.getEntityElement(expressionId).getExpression())));
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

	private static void replaceVarNameWithId(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPInfoEntityComplex complexEntityInfo = processContext.getDomainContext().getComplexEntityInfoByExecutableId(expreesionGroupEntityIdExe);
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)complexEntityInfo.getExecutable();
		HAPDefinitionEntityExpressionGroup expressionGroupDef = (HAPDefinitionEntityExpressionGroup)complexEntityInfo.getDefinition();
		HAPDefinitionEntityValueContext valueStructureComplex = complexEntityInfo.getValueStructureComplex();
		
		Map<String, HAPExecutableExpression> expressionItems = expressionGroupExe.getExpressionItems();
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
						HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(variableOperand.getVariableName(), valueStructureComplex, processContext.getDomainContext().getValueStructureDomain(), null, elementTypes); 
						variableOperand.setVariableId(new HAPComplexPath(resolve.referredRoot.getLocalId(), new HAPComplexPath(variableOperand.getVariableName()).getPath()).getFullName());
					}
					else if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
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
					}
					return true;
				}
			});
		}
	}

	//build variable into within expression item
	private static void discoverExpressionItemVariable(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPInfoEntityComplex complexEntityInfo = processContext.getDomainContext().getComplexEntityInfoByExecutableId(expreesionGroupEntityIdExe);
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)complexEntityInfo.getExecutable();
		HAPDefinitionEntityExpressionGroup expressionGroupDef = (HAPDefinitionEntityExpressionGroup)complexEntityInfo.getDefinition();
		
		HAPContainerVariableCriteriaInfo expressionGroupVarsContainer = expressionGroupExe.getVariablesInfo();
		Map<String, HAPExecutableExpression> items = expressionGroupExe.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Set<String> varIds = HAPUtilityOperand.discoverVariableIds(item.getOperand());
			
			HAPContainerVariableCriteriaInfo expressionVarsContainer = new HAPContainerVariableCriteriaInfo();
			for(String varId : varIds) {
				expressionVarsContainer.addVariable(varId, expressionGroupVarsContainer.getVariableCriteriaInfo(varId));
			}
			item.setVariablesInfo(expressionVarsContainer);
		}
	}
	

	private static void buildVariableInfoInExpression(HAPIdEntityInDomain expreesionGroupEntityIdExe, HAPContextProcessor processContext) {
		HAPInfoEntityComplex complexEntityInfo = processContext.getDomainContext().getComplexEntityInfoByExecutableId(expreesionGroupEntityIdExe);
		HAPExecutableExpressionGroup expressionGroupExe = (HAPExecutableExpressionGroup)complexEntityInfo.getExecutable();
		HAPDefinitionEntityExpressionGroup expressionGroupDef = (HAPDefinitionEntityExpressionGroup)complexEntityInfo.getDefinition();

		HAPContainerVariableCriteriaInfo varCrteriaInfoInExpression = new HAPContainerVariableCriteriaInfo();
		
		HAPContainerVariableCriteriaInfo varCrteriaInfoInStructure = HAPUtilityValueStructure.discoverDataVariablesInStructure(expressionGroupDef.getValueContextEntity(), processContext.getDomainContext().getValueStructureDomain());
		for(String varId : HAPUtilityExpression.discoverDataVariablesIdInExpression(expressionGroupExe)) {
			varCrteriaInfoInExpression.addVariable(varId, varCrteriaInfoInStructure.getVariableCriteriaInfo(varId));
		}
		expressionGroupExe.setVariablesInfo(varCrteriaInfoInExpression);
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
	private static void processConstant(HAPExecutableExpressionGroup expressionExe) {
		for(HAPExecutableExpression expressionItem : expressionExe.getExpressionItems().values()) {
			HAPUtilityOperand.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}

	
}
