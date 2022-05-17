package com.nosliw.data.core.structure.reference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPInfoValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureExecutable;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoPathToSolidRoot;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityStructureElementReference {

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInStructureComplex reference, HAPCandidatesValueStructureComplex valueStructureComplexs, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		//find all candidate value structure 
		List<HAPWrapperValueStructureExecutable> targetStructures = discoverCandidateValueStructure(valueStructureComplexs.getValueStructureComplex(reference.getParentComplexName()), reference.getValueStructureReference(), resolveConfigure, valueStructureDomain);
		
		//
		HAPInfoReferenceResolve out =  analyzeElementReference(reference.getElementPath(), targetStructures, resolveConfigure, valueStructureDomain);
		return out;
	}
	
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve analyzeElementReference(String elementPath, List<HAPWrapperValueStructureExecutable> targetStructures, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		if(targetStructures==null)   return null;
		
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPWrapperValueStructureExecutable structureWrapper : targetStructures) {
			String valueStructureExeId = structureWrapper.getValueStructureRuntimeId();
			HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureExeId);
			HAPComplexPath complexPath = new HAPComplexPath(elementPath);
			String rootName = complexPath.getRoot();
			String path = complexPath.getPathStr();
			
			HAPRootStructure root = valueStructure.getRootByName(rootName);
			if(root!=null) {
				HAPInfoReferenceResolve resolved = new HAPInfoReferenceResolve(); 
				resolved.structureId = valueStructureExeId;
				resolved.rootName = rootName;
				resolved.referredRoot = root;

				resolved.elementInfoSolid = HAPUtilityStructure.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
				resolved.elementInfoOriginal = HAPUtilityStructure.resolveDescendant(root.getDefinition(), path);
				
				if(resolved!=null) {
					Set<String> elementTypes = resolveConfigure.getCandidateElementTypes();
					if(elementTypes==null || elementTypes.contains(resolved.elementInfoSolid.resolvedElement.getType())) {
						resolveCandidates.add(resolved);
						if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(resolveConfigure.getSearchMode()))   break;
					}
				}
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPInfoReferenceResolve out = null;
		int length = 99999;
		for(HAPInfoReferenceResolve candidate : resolveCandidates) {
			HAPPath remainingPath = candidate.elementInfoSolid.remainPath;
			if(remainingPath.isEmpty()) {
				out = candidate;
				break;
			}
			else {
				if(remainingPath.getLength()<length) {
					length = remainingPath.getLength();
					out = candidate;
				}
			}
		}
		return out;
	}

	//find all value structure which meet criteria from value structure complex
	private static List<HAPWrapperValueStructureExecutable> discoverCandidateValueStructure(String valueStructureComplexId, HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		List<HAPWrapperValueStructureExecutable> out = new ArrayList<HAPWrapperValueStructureExecutable>();
		
		HAPExecutableEntityComplexValueStructure valueStructureComplex = valueStructureDomain.getValueStructureComplex(valueStructureComplexId);
		List<HAPInfoPartSimple> allSimpleParts = HAPUtilityComplexValueStructure.getAllSimpleParts(valueStructureComplex);
		for(HAPInfoPartSimple simplePart : allSimpleParts) {
			for(HAPWrapperValueStructureExecutable wraper : simplePart.getSimpleValueStructurePart().getValueStructures()) {
				boolean isValid = true;

				HAPInfoValueStructure valueStructureDefInfo = valueStructureDomain.getValueStructureDefInfoByRuntimeId(wraper.getValueStructureRuntimeId());

				//check group type
				if(isValid) {
					Set<String> groupTypes = resolveConfigure.getValueStructureGroupTypes();
					if(groupTypes!=null&&!groupTypes.isEmpty()) {
						if(!groupTypes.contains(wraper.getGroupType())) {
							isValid = false;
						}
					}
				}

				//check definition id
				if(isValid) {
					String valueStructueDefId = valueStructureCriteria.getDefinitionId();
					if(valueStructueDefId!=null) {
						if(!valueStructueDefId.equals(valueStructureDomain.getValueStructureDefinitionIdByRuntimeId(wraper.getValueStructureRuntimeId()))){
							isValid = false;
						}
					}
				}
				
				//check name
				if(isValid) {
					String valueStructureName = valueStructureCriteria.getName();
					if(valueStructureName!=null) {
						if(!valueStructureDefInfo.getExtraInfo().getName().equals(valueStructureName)){
							isValid = false;
						}
					}
				}
				
				if(isValid)  out.add(wraper);
			}
		}
		return out;
	}
	
	
	//resolve the remain path part
	public static HAPElementStructure resolveElement(HAPInfoDesendantResolve resolveInfo, Boolean relativeInheritRule) {
		HAPElementStructure out = null;
		if(relativeInheritRule==null)   relativeInheritRule = true;
		
		if(resolveInfo.remainPath.isEmpty()) {
			//exactly match with path
			out = resolveInfo.resolvedElement;
		}
		else {
			//nof exactly match with path
			HAPElementStructure candidateNode = resolveInfo.resolvedElement.getSolidStructureElement();
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA.equals(candidateNode.getType())) {
				//data type node
				HAPElementStructureLeafData dataLeafEle = (HAPElementStructureLeafData)candidateNode;
				HAPDataTypeCriteria childCriteria = HAPUtilityCriteria.getChildCriteriaByPath(dataLeafEle.getCriteria(), resolveInfo.remainPath.getPath());
				if(childCriteria!=null) {
					out = new HAPElementStructureLeafData(new HAPVariableDataInfo(childCriteria)); 
					
					//inherit rule from parent
					if(relativeInheritRule) {
						HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)candidateNode).getDataInfo();
						for(HAPDataRule rule : solidParentDataInfo.getRules()) {
							String subPath = null;
							String rulePath = rule.getPath();
							String remainPath = resolveInfo.remainPath.getPath();
							if(HAPBasicUtility.isEquals(rulePath, remainPath))  subPath = "";
							else if(remainPath.contains(rulePath+"."))  subPath = remainPath.substring(rulePath.length()+1);

							if(subPath!=null) {
								HAPDataRule newRule = rule.cloneDataRule();
								newRule.setPath(subPath);
								((HAPElementStructureLeafData)out).getDataInfo().addRule(newRule);
							}
						}
					}
					
				}
				else {
//					out.resolvedNode = new HAPContextDefinitionLeafValue();
				}
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE.equals(candidateNode.getType())){
				out = candidateNode;
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(candidateNode.getType())){
				//kkkkkk
				out = candidateNode;
			}
		}
		return out;
	}
	

	
	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInStructureComplex reference, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		return resolveElementReference(reference.getElementPath(), parentStructures.getStructure(reference.getParentComplexName()), mode, relativeInheritRule, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		HAPInfoReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		if(resolveInfo!=null)  resolveInfo.resolvedElement = resolveElement(resolveInfo.realSolidSolved, relativeInheritRule);
		return resolveInfo;
	}
	
	public static HAPInfoReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElementInStructure elementReference = new HAPReferenceElementInStructure(elementReferenceLiterate); 
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}

	public static HAPInfoReferenceResolve resolveElementReference(String reference, HAPDefinitionEntityComplexValueStructure parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		return resolveElementReference(new HAPReferenceElementInStructureComplex(reference), parentValueStructureComplex, valueStructureDomain, mode, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInStructureComplex reference, HAPDefinitionEntityComplexValueStructure parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		List<HAPInfoPartSimple> candidates = HAPUtilityComplexValueStructure.findCandidateSimplePart(reference.getParentComplexName(), parentValueStructureComplex);
		for(HAPInfoPartSimple candidate : candidates) {
			HAPPartComplexValueStructureSimple simplePart = candidate.getSimpleValueStructurePart();
			HAPValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
			HAPInfoReferenceResolve resolve = analyzeElementReference(new HAPReferenceElementInStructure(reference.getElementPath()), valueStructure, mode, elementTypes);
			resolve.structureId = simplePart.getRuntimeId();
			if(isLogicallySolved(resolve))  return resolve;
		}
		return null;
	}
	
	//find exact physical node
	public static boolean isPhysicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && (solve.elementInfoOriginal!=null && solve.elementInfoSolid.remainPath.isEmpty());
	}

	//find node
	public static boolean isLogicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && solve.elementInfoOriginal!=null;
	}

	public static HAPStructure getReferedStructure(String name, HAPContainerStructure parents, HAPStructure self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name))  return self;
		else return parents.getStructure(name);
	}

	public static void processRelativeInStructure(HAPDefinitionEntityValueStructure valueStructure, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure, HAPConfigureProcessorStructure configure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPRootStructure rootStructure : valueStructure.getAllRoots()) {
			HAPElementStructure rootElement = processRelativeInStructureElement(new HAPInfoElement(rootStructure.getDefinition(), new HAPComplexPath(rootStructure.getName())), parentValueStructureComplex, valueStructureDomain, resolveConfigure, configure, dependency, errors, runtimeEnv);
			rootStructure.setDefinition(rootElement);
		}
	}
	
	private static HAPElementStructure processRelativeInStructureElement(HAPInfoElement structureEleInfo, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure, HAPConfigureProcessorStructure configure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructure defStructureElement = structureEleInfo.getElement();
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementStructureLeafRelative relativeStructureElement = (HAPElementStructureLeafRelative)defStructureElement;
			if(dependency!=null)  dependency.add(relativeStructureElement.getPath().getParentComplexName());
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeContextDefinitionElement(structureEleInfo, parentValueStructureComplex, valueStructureDomain, resolveConfigure, configure, errors, runtimeEnv);
			}
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInStructureElement(new HAPInfoElement(nodeStructureElement.getChild(childName), structureEleInfo.getElementPath().appendSegment(childName)), parentValueStructureComplex, valueStructureDomain, resolveConfigure, configure, dependency, errors, runtimeEnv));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeContextDefinitionElement(HAPInfoElement structureEleInfo, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure, HAPConfigureProcessorStructure configure, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructureLeafRelative defStructureElementRelative = (HAPElementStructureLeafRelative)structureEleInfo.getElement();
		HAPElementStructure out = defStructureElementRelative;
		
		HAPReferenceElementInStructureComplex pathReference = defStructureElementRelative.getPath();
		HAPInfoReferenceResolve resolveInfo = resolveElementReference(pathReference, parentValueStructureComplex, resolveConfigure, valueStructureDomain);

		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
			{
				//if refer to a constant element
				out = new HAPElementStructureLeafConstant();
				Object constantValue = ((HAPElementStructureLeafConstant)resolveInfo.referredRoot.getDefinition()).getValue();
				((HAPElementStructureLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				HAPElementStructure solvedContextEle = resolveInfo.finalElement; 
				if(solvedContextEle!=null){
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						String refPath = null;
						HAPElementStructure parentContextEle = resolveInfo.elementInfoOriginal.resolvedElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = HAPUtilityNamingConversion.cascadePath(parentSolidNodeRef.getPath().getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
						}
						else {
							refRootId = resolveInfo.referredRoot.getId();
							refPath = HAPUtilityNamingConversion.cascadePath(resolveInfo.elementInfoSolid.solvedPath.getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
						}
						defStructureElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, new HAPPath(refPath)));
					}
					
					HAPElementStructure relativeContextEle = defStructureElementRelative.getDefinition();
					if(relativeContextEle==null) {
						defStructureElementRelative.setDefinition(solvedContextEle.cloneStructureElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityStructure.mergeElement(solvedContextEle, relativeContextEle, false, matchers, null, runtimeEnv);
						//remove all the void matchers
						Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
						for(String p : matchers.keySet()){
							HAPMatchers match = matchers.get(p);
							if(!match.isVoid()){
								noVoidMatchers.put(p, match);
							}
						}
						defStructureElementRelative.setMatchers(noVoidMatchers);
						
						//inherit rule from parent
						if(configure.relativeInheritRule) {
							HAPElementStructure solidParent = solvedContextEle.getSolidStructureElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeContextEle).getDataInfo();
									relativeDataInfo.addRule(rule);
									relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
								}
							}
						}
					}
				}
				else{
					//not find parent node, throw exception
					errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
				}
			}
			}			
		}

		return out;
	}


}
