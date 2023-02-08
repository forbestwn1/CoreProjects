package com.nosliw.data.core.structure.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPInfoValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutablePartValueContextSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoValueStructureSorting;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperExecutableValueStructure;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityStructureElementReference {

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueContext reference, HAPCandidatesValueContext valueContexts, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		//find all candidate value structure 
		List<HAPWrapperExecutableValueStructure> targetStructures = discoverCandidateValueStructure(valueContexts.getValueContext(reference.getParentValueContextName()), reference.getValueStructureReference(), resolveConfigure, valueStructureDomain);
		
		//resolve targeted structure element
		HAPInfoReferenceResolve out =  analyzeElementReference(reference.getElementPath(), targetStructures, resolveConfigure, valueStructureDomain);
		out.eleReference = reference;
		
		return out;
	}
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve analyzeElementReference(String elementPath, List<HAPWrapperExecutableValueStructure> targetStructures, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		if(targetStructures==null)   return null;

		if(resolveConfigure==null)	resolveConfigure = new HAPConfigureResolveStructureElementReference();
		
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPWrapperExecutableValueStructure structureWrapper : targetStructures) {
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

				resolved.elementInfoSolid = HAPUtilityStructure.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
				if(resolved.elementInfoSolid!=null) {
					resolved.elementInfoOriginal = HAPUtilityStructure.resolveDescendant(root.getDefinition(), path);
					
					Set<String> elementTypes = resolveConfigure.candidateElementTypes;
					if(elementTypes==null || elementTypes.contains(resolved.elementInfoSolid.resolvedElement.getType())) {
						resolveCandidates.add(resolved);
						if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(resolveConfigure.searchMode))   break;
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
				//all path solved
				out = candidate;
				break;
			}
			else {
				//some remaining path unsolved, find the shortest one 
				if(remainingPath.getLength()<length) {
					length = remainingPath.getLength();
					out = candidate;
				}
			}
		}
		return out;
	}

	//find all value structure which meet criteria from value structure complex
	private static List<HAPWrapperExecutableValueStructure> discoverCandidateValueStructure(HAPExecutableEntityValueContext valueStructureComplex, HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveStructureElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain){
		List<HAPWrapperExecutableValueStructure> out = new ArrayList<HAPWrapperExecutableValueStructure>();
		
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructuresSorted(valueStructureComplex);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			HAPWrapperExecutableValueStructure wraper = valueStructureInfo.getValueStructure();
			boolean isValid = true;

			HAPInfoValueStructure valueStructureDefInfo = valueStructureDomain.getValueStructureDefInfoByRuntimeId(wraper.getValueStructureRuntimeId());

			//check group type
			if(isValid) {
				Set<String> groupTypes = resolveConfigure==null?null:resolveConfigure.valueStructureGroupTypes;
				if(groupTypes!=null&&!groupTypes.isEmpty()) {
					if(!groupTypes.contains(wraper.getGroupType())) {
						isValid = false;
					}
				}
			}

			//check definition id
			if(isValid) {
				String valueStructueDefId = valueStructureCriteria==null? null : valueStructureCriteria.getDefinitionId();
				if(valueStructueDefId!=null) {
					if(!valueStructueDefId.equals(valueStructureDomain.getValueStructureDefinitionIdByRuntimeId(wraper.getValueStructureRuntimeId()))){
						isValid = false;
					}
				}
			}
			
			//check name
			if(isValid) {
				String valueStructureName = valueStructureCriteria==null? null : valueStructureCriteria.getName();
				if(valueStructureName!=null) {
					if(!valueStructureDefInfo.getExtraInfo().getName().equals(valueStructureName)){
						isValid = false;
					}
				}
			}
			
			if(isValid)  out.add(wraper);
		}
		return out;
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

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueContext reference, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		return resolveElementReference(reference.getElementPath(), parentStructures.getStructure(reference.getParentValueContextName()), mode, relativeInheritRule, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		HAPInfoReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		if(resolveInfo!=null)  resolveInfo.resolvedElement = resolveFinalElement(resolveInfo.realSolidSolved, relativeInheritRule);
		return resolveInfo;
	}
	
	public static HAPInfoReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElementInStructure elementReference = new HAPReferenceElementInStructure(elementReferenceLiterate); 
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}

	public static HAPInfoReferenceResolve resolveElementReference(String reference, HAPDefinitionEntityValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		return resolveElementReference(new HAPReferenceElementInValueContext(reference), parentValueStructureComplex, valueStructureDomain, mode, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueContext reference, HAPDefinitionEntityValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		List<HAPInfoPartSimple> candidates = HAPUtilityValueContext.findCandidateSimplePart(reference.getParentValueContextName(), parentValueStructureComplex);
		for(HAPInfoPartSimple candidate : candidates) {
			HAPExecutablePartValueContextSimple simplePart = candidate.getSimpleValueStructurePart();
			HAPValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
			HAPInfoReferenceResolve resolve = analyzeElementReference(new HAPReferenceElementInStructure(reference.getElementPath()), valueStructure, mode, elementTypes);
			resolve.structureId = simplePart.getRuntimeId();
			if(isLogicallySolved(resolve))  return resolve;
		}
		return null;
	}
}
