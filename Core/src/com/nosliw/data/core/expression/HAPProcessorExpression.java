package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.component.HAPUtilityComponentConstant;
import com.nosliw.data.core.component.HAPWithComplexEntity;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPUtilityDataAssociation;
import com.nosliw.data.core.dataassociation.mapping.HAPUtilityStructureDataAssociation;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public class HAPProcessorExpression {

	public static HAPExecutableExpressionGroup process(
			String id,
			HAPDefinitionExpressionGroup expressionGroupDef,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			Map<String, HAPDataTypeCriteria> expectOutput,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		//create exectuable 
		HAPExecutableExpressionGroupInSuite out = createExecutable(id, expressionGroupDef, null, attachmentReferenceContext, runtimeEnv, processTracker);

		//expand all reference
		expandReference(out, attachmentReferenceContext, runtimeEnv, processTracker);
		
		//constant --- update constant data in expression
		processConstant(out);
		
		//normalize variable(discovery new variable from operand or input mapping for reference)
		normalizeVariable(out);
		
		//replace name with variable id
		replaceVarNameWithId(out);
		
		//build variable name mapping in reference
		buildReferencesVariableNameMapping(out);
		
		//discovery
		
		
		
		HAPExecutableExpressionGroup out = processBasic(id, expressionGroupDef, attachmentReferenceContext, expressionMan, configure, runtimeEnv, processTracker);
		
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
						normalizeVariable(referenceOperand.getReferedExpression());
					}
					return true;
				}
			});
		}

		//normalize variable from reference variable mapping first
		for(String name : expressionItems.keySet()) {
			HAPExecutableExpression expressionItem = expressionItems.get(name);
			HAPOperandUtility.processAllOperand(expressionItem.getOperand(), null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						
						//variable mapping
						HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
						HAPContainerVariableCriteriaInfo expressionVarsContainer = expressionExe.getVarsInfo();
						HAPContainerVariableCriteriaInfo referenceExpContainer = referenceOperand.getReferedExpression().getVarsInfo();
						String inputMappingType = inputMapping.getType();
						if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
							HAPDefinitionDataAssociationMapping mappingDataAssociation = (HAPDefinitionDataAssociationMapping)inputMapping;
							HAPMapping mapping = mappingDataAssociation.getMapping();
							//expand mapping so that all variable in reference expression got mapped
							mapping = HAPUtilityStructureDataAssociation.expandMappingByTargetVariable(mapping, expressionVarsContainer);
							
							//normalize variable expression according to reference input mapping
							for(HAPRoot mappingRoot : mapping.getAllRoots()) {
								HAPUtilityStructure.traverseElement(mappingRoot, new HAPProcessorContextDefinitionElement() {
									@Override
									public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
										if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
											HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)eleInfo.getElement();
											String mappingSourcePath = relativeEle.getReferencePath();
											Set<String> types = new HashSet<String>();
											types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
											types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT);
											HAPInfoReferenceResolve resolve = HAPUtilityStructure.analyzeElementReference(mappingSourcePath, expressionExe.getStructureDefinition(), null, types);
											if(resolve.referredRoot==null || resolve.realSolidSolved.remainPath!=null) {
												//path in relative element in mapping cannot be resolved
												HAPRoot root = resolve.referredRoot;
												if(root==null) {
													root = HAPUtilityStructure.addRoot(expressionExe.getStructureDefinition(), new HAPComplexPath(mappingSourcePath).getRootName(), new HAPRoot());
												}
												HAPDataTypeCriteria dataTyppeCriteria = referenceExpContainer.getVariableInfoByAlias(eleInfo.getElementPath().getFullName()).getCriteriaInfo().getCriteria();
												HAPUtilityStructure.setDescendant(resolve.referredRoot, new HAPComplexPath(mappingSourcePath).getPath(), new HAPElementLeafData(dataTyppeCriteria));
											}
										
										}
										return null;
									}

									@Override
									public void postProcess(HAPInfoElement eleInfo, Object value) {	}
								}, null);
							}
							
						}
						else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
							
							
							
							
							Set<String> missedRefVars = referenceExpContainer.findMissingVariables(expressionVarsContainer.getDataVariableNames());
							for(String missedRefVar : missedRefVars) {
								//if variable in referred expression is not mapped, then pop up the variable to parent
								Set<String> aliases = referenceExpContainer.getVariableAlias(missedRefVar);
								boolean override = expressionVarsContainer.addVariableCriteriaInfo(referenceExpContainer.getVariableCriteriaInfoById(missedRefVar).cloneCriteriaInfo(), aliases);
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
						if(resolve.referredRoot==null || resolve.realSolidSolved.remainPath!=null) {
							//variable name cannot be resolved
							HAPRoot root = resolve.referredRoot;
							if(root==null) {
								root = HAPUtilityStructure.addRoot(expressionExe.getStructureDefinition(), new HAPComplexPath(variablePath).getRootName(), new HAPRoot());
							}
							HAPUtilityStructure.setDescendant(resolve.referredRoot, new HAPComplexPath(variablePath).getPath(), new HAPElementLeafData());
						}
						
					}
					return true;
				}
			});
		}
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
		constants.putAll(HAPUtilityComponentConstant.getConstantsData(expressionGroupDef, out.getContextFlat()));
		out.setDataConstants(constants);
		
		//variable --- from context
		HAPContainerVariableCriteriaInfo varsContainer = HAPUtilityValueStructure.discoverDataVariablesInStructure(out.getStructureDefinition());
		out.setVarsInfo(varsContainer);

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
						String refName = referenceOperand.getReferenceName();
						
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
							expressionGroupDefiniton = (HAPDefinitionExpressionGroup)runtimeEnv.getResourceDefinitionManager().getResourceDefinition(expressionResourceId);
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
	
	private static void replaceVarNameWithId(HAPExecutableExpressionGroup expressionExe) {
		Map<String, HAPExecutableExpression> expressionItems = expressionExe.getExpressionItems();
		
		HAPStructure structure = expressionExe.getStructureDefinition();
		
		//normalize child reference expression first
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
						replaceVarNameWithId(referenceOperand.getReferedExpression());
						referenceOperand.setInputMapping(HAPUtilityStructureDataAssociation.replaceVarNameWithId(referenceOperand.getInputMapping(), referenceOperand.getReferedExpression().getStructureDefinition(), expressionExe.getStructureDefinition()));
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
							HAPStructureValueDefinitionFlat da = mappingDa.getAssociation();
							for(String rootName : da.getRootNames()) {
								Map<String, String> path = HAPUtilityDataAssociation.buildSimplifiedRelativePathMapping(da.getRoot(rootName), rootName, expressionExe.getStructureDefinition());
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

	
	
	
	
	
	
	
	
	private static HAPExecutableExpressionGroup processBasic(
			String exeId,
			HAPDefinitionExpressionGroup expressionGroupDef,
			HAPContextProcessAttachmentReferenceExpression attachmentReferenceContext,
			HAPManagerExpression expressionMan,
			Map<String, String> configure,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPExecutableExpressionGroupInSuite out = new HAPExecutableExpressionGroupInSuite(exeId);

		//context
		HAPStructureValueDefinition valueStructure =  expressionGroupDef.getValueStructure();
		out.setStructureDefinition(valueStructure);

		//constant
		//constant --- discover constant from attachment and context
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>(); 
		constants.putAll(HAPUtilityComponentConstant.getConstantsData(expressionGroupDef, out.getContextFlat()));
		
		
		//expand referenced expression
		for(HAPExecutableExpression expressionItem : out.getExpressionItems().values()) {
			processReferencesInOperandBasic(exeId, expressionItem.getOperand(), attachmentReferenceContext, expressionMan, configure, runtimeEnv, processTracker);
		}
		
		
		
		
		
		
		//variable
		//replace variable name with id
		Set<String> eleTypes = new HashSet<String>();
		eleTypes.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA);
		for(HAPExecutableExpression expressionItem : out.getExpressionItems().values()) {
			Set<HAPOperandWrapper> varOperands = HAPOperandUtility.discoverVariableOperands(expressionItem.getOperand());
			for(HAPOperandWrapper operand : varOperands) {
				HAPOperandVariable varOperand =  (HAPOperandVariable)operand.getOperand();
				String varName = varOperand.getVariableName();
				HAPInfoReferenceResolve resolve = HAPUtilityStructure.resolveElementReference(varName, valueStructure, HAPConstant.RESOLVEPARENTMODE_BEST, eleTypes);
				if(HAPUtilityStructure.isLogicallySolved(resolve)) {
					//able to resolve the variable, replace root name with root local id
					varOperand.setVariableName(new HAPComplexPath(varName).updateRootName(resolve.referredRoot.getLocalId()).getFullName());
				}
				else {
					//cannot resolve the variable
					
				}
			}
		}

		
		
		
		//variable --- from context
		HAPContainerVariableCriteriaInfo varsContainer = HAPUtilityContext.discoverDataVariablesInStructure(out.getContextFlat());
		out.setVarsInfo(varsContainer);

		Set<HAPDefinitionExpression> expressionDefs = expressionGroupDef.getEntityElements();
		for(HAPDefinitionExpression expressionDef : expressionDefs) {
			out.addExpression(expressionDef.getId(), new HAPOperandWrapper(expressionMan.getExpressionParser().parseExpression(expressionDef.getExpression())));
		}

		//add missed variable to variable info container
		for(String varName : unknownVarNames) 	varsContainer.addVariableCriteriaInfo(HAPInfoCriteria.buildUndefinedCriteriaInfo(), varName, varName);

		//attribute chain --- consolidate attribute chain to single variable or constant operand
		for(HAPExecutableExpression expressionItem : out.getExpressionItems().values()) {
			HAPOperandUtility.processAttributeOperandInExpressionOperand(expressionItem.getOperand(), varsContainer.getDataVariableNames(), constants.keySet());
		}

		//constant --- update constant data in expression
		for(HAPExecutableExpression expressionItem : out.getExpressionItems().values()) {
			HAPOperandUtility.updateConstantData(expressionItem.getOperand(), constants);
		}
		
		//variable --- replace variable rootroot name with variable id
		for(HAPExecutableExpression expressionItem : out.getExpressionItems().values()) {
			HAPOperandUtility.updateNameInOperand(expressionItem.getOperand(), varsContainer.buildToVarIdNameUpdate(), new String[] {HAPConstantShared.EXPRESSION_OPERAND_VARIABLE});
		}
		
		return out;
	}

	//build variable into within expression item
	private static void discoverExpressionItemVariable(HAPExecutableExpressionGroup expression) {
		HAPContainerVariableCriteriaInfo expressionVarsContainer = expression.getVarsInfo();
		Map<String, HAPExecutableExpression> items = expression.getExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpression item = items.get(name);
			Set<String> varNames = HAPOperandUtility.discoverVariableNames(item.getOperand());
			HAPContainerVariableCriteriaInfo itemVarsInfo = expressionVarsContainer.buildSubContainer(varNames);
			item.setVariablesInfo(itemVarsInfo);
			
			
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
							HAPStructureValueDefinitionFlat da = mappingDa.getAssociation();
							HAPStructureValueDefinitionFlat da1 = new HAPStructureValueDefinitionFlat();
							for(String rootName : da.getRootNames()) {
								da1.addRoot(nameUpdate.getUpdatedName(rootName), da.getRoot(rootName));
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
	
}
