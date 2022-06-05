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
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureExecutable;
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
					Set<String> groupTypes = resolveConfigure.valueStructureGroupTypes;
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

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInStructureComplex reference, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		return resolveElementReference(reference.getElementPath(), parentStructures.getStructure(reference.getParentComplexName()), mode, relativeInheritRule, elementTypes);
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
}
