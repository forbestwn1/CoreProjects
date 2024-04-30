package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPReferenceBrickLocal;
import com.nosliw.core.application.common.structure.HAPReferenceElementInStructure;
import com.nosliw.core.application.common.structure.HAPStructure1;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.variable.HAPIdRootElement;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.valuecontext.HAPExecutablePartValueContextSimple;
import com.nosliw.data.core.domain.valuecontext.HAPInfoPartSimple;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;

public class HAPUtilityStructureElementReference {

	public static HAPIdRootElement resolveValueStructureRootReference(HAPReferenceRootElement rootEleCriteria, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle){
		HAPResultReferenceResolve resolve = analyzeElementReference(new HAPReferenceElement(rootEleCriteria), resolveConfigure, bundle);
		return new HAPIdRootElement(rootEleCriteria.getValuePortRef(), resolve.structureId, rootEleCriteria.getRootName());
	}
	
	public static HAPResultReferenceResolve analyzeElementReference(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle) {
		return analyzeElementReference(reference, resolveConfigure, null, bundle);
	}

	
	public static HAPResultReferenceResolve analyzeElementReference(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPPath defaultBrickPath, HAPBundle bundle) {
		Triple<HAPReferenceBrickLocal, HAPIdValuePort, HAPValuePort> valuePortInfo = HAPUtilityValuePort.getValuePort(reference.getValuePortRef(), defaultBrickPath, bundle);

		HAPResultReferenceResolve resolve  = valuePortInfo.getRight().resolveReference(reference, resolveConfigure);
		if(resolve!=null) {
			resolve.brickReference = valuePortInfo.getLeft();
			resolve.valuePortId = valuePortInfo.getMiddle();
			resolve.elementPath = reference.getElementPath();
		}
		return resolve;
	}
	
	//find best resolved element from structure 
	public static HAPResultReferenceResolve analyzeElementReference(String elementPath, List<HAPInfoValueStructureReference> targetStructures, HAPConfigureResolveElementReference resolveConfigure){
		if(targetStructures==null) {
			return null;
		}

		if(resolveConfigure==null) {
			resolveConfigure = new HAPConfigureResolveElementReference();
		}
		
		List<HAPResultReferenceResolve> resolveCandidates = new ArrayList<HAPResultReferenceResolve>();
		for(HAPInfoValueStructureReference valueStructureInfo : targetStructures) {
			HAPValueStructureInValuePort valueStructure = valueStructureInfo.getValueStructureDefinition();
			HAPComplexPath complexPath = new HAPComplexPath(elementPath);
			String rootName = complexPath.getRoot();
			String path = complexPath.getPathStr();
			
			HAPRootStructureInValuePort root = valueStructure.getRootByName(rootName);
			if(root!=null) {
				HAPResultReferenceResolve resolved = new HAPResultReferenceResolve(); 
				resolved.structureId = valueStructureInfo.getValueStructureId();
				resolved.rootName = rootName;
				resolved.elementPath = path;
				resolved.fullPath = elementPath;

				resolved.elementInfoSolid = HAPUtilityStructure.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
				if(resolved.elementInfoSolid!=null) {
					resolved.elementInfoOriginal = HAPUtilityStructure.resolveDescendant(root.getDefinition(), path);
					
					Set<String> elementTypes = resolveConfigure.candidateElementTypes;
					if(elementTypes==null || elementTypes.contains(resolved.elementInfoSolid.resolvedElement.getType())) {
						resolveCandidates.add(resolved);
						if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(resolveConfigure.searchMode)) {
							break;
						}
					}
				}
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPResultReferenceResolve out = null;
		int length = 99999;
		for(HAPResultReferenceResolve candidate : resolveCandidates) {
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


	
	
	
	
	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(reference.getValuePortRef(), processContext);
		List<HAPInfoValueStructureReference> valueStructureInfos = valuePort.discoverCandidateValueStructure(reference.getValueStructureReference());
		
		//resolve targeted structure element
		HAPResultReferenceResolve out =  analyzeElementReference(reference.getElementPath(), valueStructureInfos, resolveConfigure);
		if(out!=null) {
			out.eleReference = reference;
		}
		
		return out;
	}

	public static HAPIdRootElement resolveValueStructureRootReference1(HAPReferenceRootElement rootEleCriteria, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(rootEleCriteria.getValuePortRef(), processContext);
		List<HAPInfoValueStructureReference> candidates = valuePort.discoverCandidateValueStructure(rootEleCriteria.getValueStructureReference());

		if(candidates==null||candidates.size()==0) {
			return null;
		}
		for(HAPInfoValueStructureReference structureRefInfo : candidates) {
			String valueStructureExeId = structureRefInfo.getValueStructureId();
			HAPManualBrickValueStructure valueStructure = structureRefInfo.getValueStructureDefinition();
			String rootName = rootEleCriteria.getRootName();
			if(valueStructure.getRootByName(rootName)!=null) {
				return new HAPIdRootElement(rootEleCriteria.getValuePortRef(), valueStructureExeId, rootName);
			}
		}
		return null;
	}
	
	//find exact physical node
	public static boolean isPhysicallySolved(HAPResultReferenceResolve solve) {
		return solve!=null && (solve.elementInfoOriginal!=null && solve.elementInfoSolid.remainPath.isEmpty());
	}

	//find node
	public static boolean isLogicallySolved(HAPResultReferenceResolve solve) {
		return solve!=null && solve.elementInfoOriginal!=null;
	}

	public static HAPStructure1 getReferedStructure(String name, HAPContainerStructure parents, HAPStructure1 self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name)) {
			return self;
		} else {
			return parents.getStructure(name);
		}
	}

	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		return resolveElementReference(reference.getPath(), parentStructures.getStructure(reference.getParentValueContextName()), mode, relativeInheritRule, elementTypes);
	}
	
	public static HAPResultReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure1 parentStructure, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		HAPResultReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		if(resolveInfo!=null) {
			resolveInfo.resolvedElement = resolveFinalElement(resolveInfo.realSolidSolved, relativeInheritRule);
		}
		return resolveInfo;
	}
	
	public static HAPResultReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure1 parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElementInStructure elementReference = new HAPReferenceElementInStructure(elementReferenceLiterate); 
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}

	public static HAPResultReferenceResolve resolveElementReference(String reference, HAPManualBrickValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		return resolveElementReference(new HAPReferenceElement(reference), parentValueStructureComplex, valueStructureDomain, mode, elementTypes);
	}
	
	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPManualBrickValueContext parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, String mode, Set<String> elementTypes) {
		List<HAPInfoPartSimple> candidates = HAPUtilityValueContext.findCandidateSimplePart(reference.getParentValueContextName(), parentValueStructureComplex);
		for(HAPInfoPartSimple candidate : candidates) {
			HAPExecutablePartValueContextSimple simplePart = candidate.getSimpleValueStructurePart();
			HAPValueStructureInValuePort valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
			HAPResultReferenceResolve resolve = analyzeElementReference(new HAPReferenceElementInStructure(reference.getPath()), valueStructure, mode, elementTypes);
			resolve.structureId = simplePart.getRuntimeId();
			if(isLogicallySolved(resolve)) {
				return resolve;
			}
		}
		return null;
	}
}
