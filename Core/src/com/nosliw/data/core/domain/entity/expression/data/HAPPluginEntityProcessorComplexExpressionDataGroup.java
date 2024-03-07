package com.nosliw.data.core.domain.entity.expression.data;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;
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
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPPluginEntityProcessorComplexExpressionDataGroup extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexExpressionDataGroup(String entityType) {
		super(entityType, HAPExecutableEntityExpressionDataGroup.class);
	}

	@Override
	public void processValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableEntityExpressionDataGroup executableExpresionGroup = (HAPExecutableEntityExpressionDataGroup)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		HAPDefinitionEntityExpressionDataGroup definitionExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)defEntityInfo.getEntity();
		
		//build expression in executable
		buildExpression(null, executableExpresionGroup, definitionExpressionGroup);
		
		//build constant value for expression
		processConstant(executableExpresionGroup, processContext);
		
	}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		//resolve variable name
		resolveVariableName((HAPExecutableEntityExpressionDataGroup)complexEntityExecutable, processContext);
	}
	
	//matcher
	@Override
	public void postProcessValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableEntityExpressionDataGroup executableExpresionGroup = (HAPExecutableEntityExpressionDataGroup)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPInfoEntityInDomainDefinition defEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId);
		
		//build all variables info in expression group
		HAPUtilityExpressionProcessor.buildVariableInfo(executableExpresionGroup.getVariablesInfo(), valueStructureDomain);
		
		//process mapping in reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(executableExpresionGroup, processContext);
		
		//discovery
		if(HAPUtilityExpressionProcessConfigure.isDoDiscovery(defEntityInfo.getExtraInfo().getInfo())){
			//do discovery
			executableExpresionGroup.discover(null, processContext.getRuntimeEnvironment().getDataTypeHelper(), processContext.getProcessTracker());

			//update value context according to variable info
			HAPUtilityExpressionProcessor.updateValueContext(executableExpresionGroup.getVariablesInfo(), valueStructureDomain);
		}
	}
	
	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {

		//process referenced expression
//		HAPUtilityExpressionProcessor.processReferencedExpression(complexEntityExecutableId, processContext);
		
		//build variable into within expression item
		HAPUtilityExpressionProcessor.buildVariableInfoInExpression((HAPExecutableEntityExpressionDataGroup)complexEntityExecutable, processContext);
	}

	//update constant operand with constant data
	private static void processConstant(HAPExecutableEntityExpressionDataGroup expressionGroupExe, HAPContextProcessor processContext) {
		for(HAPExecutableExpressionData expressionItem : expressionGroupExe.getAllExpressionItems()) {
			HAPUtilityExpressionProcessor.processConstant(expressionGroupExe, expressionItem, processContext);
		}
	}
	
	private static void resolveVariableName(HAPExecutableEntityExpressionDataGroup expressionGroupExe, HAPContextProcessor processContext) {
		for(HAPExecutableExpressionData expressionItem : expressionGroupExe.getAllExpressionItems()) {
			HAPUtilityExpressionProcessor.resolveVariableName(expressionItem, expressionGroupExe.getValueContext(), expressionGroupExe.getVariablesInfo(), processContext.getCurrentValueStructureDomain());
		}
	}
	
	private void buildExpression(String expressionId, HAPExecutableEntityExpressionDataGroup expressionGroupExe, HAPDefinitionEntityExpressionDataGroup expressionGroupDef) {
		//expression element
		if(expressionId==null) {
			//all elements
			List<HAPDefinitionExpressionData> expressionDefs = expressionGroupDef.getEntityElements();
			for(HAPDefinitionExpressionData expressionDef : expressionDefs) {
				expressionGroupExe.addExpressionItem(new HAPExecutableExpressionData(expressionDef));
			}
		}
		else {
			expressionGroupExe.addExpressionItem(new HAPExecutableExpressionData(expressionGroupDef.getEntityElement(expressionId)));
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//create executable 
	//in some case, executable can be created based on one expression item in group, this is case when deal with reference expression 
	private void buildConstant1(HAPExecutableEntityExpressionDataGroup expressionGroupExe, HAPDefinitionEntityExpressionDataGroup expressionGroupDef, HAPDomainValueStructure valueStructureDomain) {
		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComplexConstant.getConstantsData(expressionGroupDef, valueStructureDomain));
		expressionGroupExe.setDataConstants(constants);
	}
	
	private static Triple<HAPDefinitionEntityExpressionDataGroup, HAPExecutableEntityExpressionDataGroup, HAPDefinitionEntityValueContext> getComplexEntityByExecutableId(HAPIdEntityInDomain executableId, HAPContextProcessor processContext){
		Triple<HAPManualEntityComplex, HAPExecutableEntityComplex, HAPDefinitionEntityValueContext> triple = processContext.getDomainContext().getComplexEntityInfoByExecutableId(executableId);
		HAPExecutableEntityExpressionDataGroup expressionGroupExe = (HAPExecutableEntityExpressionDataGroup)triple.getMiddle();
		HAPDefinitionExpressionGroup1 expressionGroupDef = (HAPDefinitionExpressionGroup1)triple.getLeft();
		return Triple.of(expressionGroupDef, expressionGroupExe, triple.getRight());
	}

	
	public static HAPExecutableEntityExpressionDataGroup process(
			String id,
			HAPDefinitionExpressionGroup1 expressionGroupDef,
			HAPContextProcessor attachmentReferenceContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		//create execUtable 
		HAPExecutableEntityExpressionDataGroup out = createExecutable(id, expressionGroupDef, null, attachmentReferenceContext, runtimeEnv, processTracker);

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
	private static void normalizeVariable(HAPExecutableEntityExpressionDataGroup expressionExe) {
		Map<String, HAPExecutableExpressionData> expressionItems = expressionExe.getAllExpressionItems();
		
		//normalize child reference expression first
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpressionData expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						normalizeVariable((HAPExecutableEntityExpressionDataGroup)referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}

		//normalize variable mapping in reference operand first, so that all variables in referenced expression are mapped
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpressionData expressionItem = expressionItems.get(name);
			HAPUtilityOperand.processAllOperand(expressionItem.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						
						//go through all variables in reference expression and find those that not mapped
						Map<String, HAPWrapperOperand> refExpMapping = referenceOperand.getMapping();
						Set<String> mappedVarNames = new HashSet<String>(refExpMapping.keySet());
						HAPVariableInfoInStructure referenceExpContainer = HAPUtilityExpression.discoverDataVariablesDefinitionInStructure((HAPExecutableEntityExpressionDataGroup)referenceOperand.getReferedExpression());
						
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
			HAPExecutableExpressionData expressionItem = expressionItems.get(name);
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
	private static void processConstant1(HAPExecutableEntityExpressionDataGroup expressionExe) {
		for(HAPExecutableExpressionData expressionItem : expressionExe.getAllExpressionItems().values()) {
			HAPUtilityOperand.updateConstantData(expressionItem.getOperand(), expressionExe.getDataConstants());
		}

	}

	
}
