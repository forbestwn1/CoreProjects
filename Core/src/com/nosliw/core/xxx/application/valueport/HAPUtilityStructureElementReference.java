package com.nosliw.core.xxx.application.valueport;

public class HAPUtilityStructureElementReference {



/*	
	//resolve variable name with possible extension
	public static HAPIdElement resolveVariableName(String variableName, HAPExecutableEntityValueContext valueContext, String extensionStructureGroup, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveElementReference resolveConfigure){
		HAPIdElement out = HAPUtilityValueContextReference.resolveVariableReference(new HAPReferenceElement(variableName), null, valueContext, valueStructureDomain, resolveConfigure);
		if(out==null) {
			//not able to resolve variable
			String valueStructureRuntimId = HAPUtilityValueContext.getExtensionValueStructure(valueContext, extensionStructureGroup!=null?extensionStructureGroup:HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			if(valueStructureRuntimId!=null) {
				HAPManualBrickValueStructure vs = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureRuntimId);
				HAPComplexPath varPath = new HAPComplexPath(variableName);
				HAPUtilityStructure.setDescendant(vs, varPath, new HAPElementStructureLeafData());
				out = new HAPIdElement(new HAPIdRootElement(valueStructureRuntimId, varPath.getRoot()), varPath.getPath().toString());
			}
			else {
				throw new RuntimeException();
			}
		}
		return out;
	}
*/
	
	
	


	
/*	
	
	
	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPContextProcessor processContext){
		HAPValuePort1111 valuePort = HAPUtilityBrickValuePort.getValuePort(reference.getValuePortId(), processContext);
		List<HAPInfoValueStructureReference> valueStructureInfos = valuePort.discoverCandidateValueStructure(reference.getValueStructureReference());
		
		//resolve targeted structure element
		HAPResultReferenceResolve out =  analyzeElementReference(reference.getElementPath(), valueStructureInfos, resolveConfigure);
		if(out!=null) {
			out.eleReference = reference;
		}
		
		return out;
	}

	public static HAPIdRootElement resolveValueStructureRootReference1(HAPIdRootElement rootEleCriteria, HAPContextProcessor processContext){
		HAPValuePort1111 valuePort = HAPUtilityBrickValuePort.getValuePort(rootEleCriteria.getValuePortId(), processContext);
		List<HAPInfoValueStructureReference> candidates = valuePort.discoverCandidateValueStructure(rootEleCriteria.getValueStructureReference());

		if(candidates==null||candidates.size()==0) {
			return null;
		}
		for(HAPInfoValueStructureReference structureRefInfo : candidates) {
			String valueStructureExeId = structureRefInfo.getValueStructureId();
			HAPManualBrickValueStructure valueStructure = structureRefInfo.getValueStructureDefinition();
			String rootName = rootEleCriteria.getRootName();
			if(valueStructure.getRootByName(rootName)!=null) {
				return new HAPIdRootElement(rootEleCriteria.getValuePortId(), valueStructureExeId, rootName);
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
			HAPValueStructureInValuePort11111 valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
			HAPResultReferenceResolve resolve = analyzeElementReference(new HAPReferenceElementInStructure(reference.getPath()), valueStructure, mode, elementTypes);
			resolve.structureId = simplePart.getRuntimeId();
			if(isLogicallySolved(resolve)) {
				return resolve;
			}
		}
		return null;
	}
*/	
}
