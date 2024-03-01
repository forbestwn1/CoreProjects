package com.nosliw.data.core.structure.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutablePartValueContextSimple;
import com.nosliw.data.core.domain.valuecontext.HAPInfoPartSimple;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.domain.valueport.HAPReferenceElementInValueStructure;
import com.nosliw.data.core.domain.valueport.HAPReferenceRootElement;
import com.nosliw.data.core.domain.valueport.HAPUtilityValuePort;
import com.nosliw.data.core.domain.valueport.HAPValuePort;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.HAPStructure1;
import com.nosliw.data.core.structure.HAPUtilityStructure;

public class HAPUtilityStructureElementReference {

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueStructure reference, HAPConfigureResolveStructureElementReference resolveConfigure, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(reference.getValuePortRef(), processContext);
		List<HAPInfoValueStructureReference> valueStructureInfos = valuePort.discoverCandidateValueStructure(reference.getValueStructureReference());
		
		//resolve targeted structure element
		HAPInfoReferenceResolve out =  analyzeElementReference(reference.getElementPath(), valueStructureInfos, resolveConfigure);
		if(out!=null)  out.eleReference = reference;
		
		return out;
	}

	public static HAPIdRootElement resolveValueStructureRootReference(HAPReferenceRootElement rootEleCriteria, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(rootEleCriteria.getValuePortRef(), processContext);
		List<HAPInfoValueStructureReference> candidates = valuePort.discoverCandidateValueStructure(rootEleCriteria.getValueStructureReference());

		if(candidates==null||candidates.size()==0)  return null;
		for(HAPInfoValueStructureReference structureRefInfo : candidates) {
			String valueStructureExeId = structureRefInfo.getValueStructureId();
			HAPDefinitionEntityValueStructure valueStructure = structureRefInfo.getValueStructureDefinition();
			String rootName = rootEleCriteria.getRootName();
			if(valueStructure.getRootByName(rootName)!=null) {
				return new HAPIdRootElement(rootEleCriteria.getValuePortRef(), valueStructureExeId, rootName);
			}
		}
		return null;
	}
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve analyzeElementReference(String elementPath, List<HAPInfoValueStructureReference> targetStructures, HAPConfigureResolveStructureElementReference resolveConfigure){
		if(targetStructures==null)   return null;

		if(resolveConfigure==null)	resolveConfigure = new HAPConfigureResolveStructureElementReference();
		
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPInfoValueStructureReference valueStructureInfo : targetStructures) {
			HAPDefinitionEntityValueStructure valueStructure = valueStructureInfo.getValueStructureDefinition();
			HAPComplexPath complexPath = new HAPComplexPath(elementPath);
			String rootName = complexPath.getRoot();
			String path = complexPath.getPathStr();
			
			HAPRootStructure root = valueStructure.getRootByName(rootName);
			if(root!=null) {
				HAPInfoReferenceResolve resolved = new HAPInfoReferenceResolve(); 
				resolved.structureId = valueStructureInfo.getValueStructureId();
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

	//find exact physical node
	public static boolean isPhysicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && (solve.elementInfoOriginal!=null && solve.elementInfoSolid.remainPath.isEmpty());
	}

	//find node
	public static boolean isLogicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && solve.elementInfoOriginal!=null;
	}

	public static HAPStructure1 getReferedStructure(String name, HAPContainerStructure parents, HAPStructure1 self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name))  return self;
		else return parents.getStructure(name);
	}

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueStructure reference, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		return resolveElementReference(reference.getPath(), parentStructures.getStructure(reference.getParentValueContextName()), mode, relativeInheritRule, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure1 parentStructure, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		HAPInfoReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		if(resolveInfo!=null)  resolveInfo.resolvedElement = resolveFinalElement(resolveInfo.realSolidSolved, relativeInheritRule);
		return resolveInfo;
	}
	
	public static HAPInfoReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure1 parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElementInStructure elementReference = new HAPReferenceElementInStructure(elementReferenceLiterate); 
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}

	public static HAPInfoReferenceResolve resolveElementReference(String reference, HAPDefinitionEntityValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		return resolveElementReference(new HAPReferenceElementInValueStructure(reference), parentValueStructureComplex, valueStructureDomain, mode, elementTypes);
	}
	
	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueStructure reference, HAPDefinitionEntityValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		List<HAPInfoPartSimple> candidates = HAPUtilityValueContext.findCandidateSimplePart(reference.getParentValueContextName(), parentValueStructureComplex);
		for(HAPInfoPartSimple candidate : candidates) {
			HAPExecutablePartValueContextSimple simplePart = candidate.getSimpleValueStructurePart();
			HAPValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
			HAPInfoReferenceResolve resolve = analyzeElementReference(new HAPReferenceElementInStructure(reference.getPath()), valueStructure, mode, elementTypes);
			resolve.structureId = simplePart.getRuntimeId();
			if(isLogicallySolved(resolve))  return resolve;
		}
		return null;
	}
}
