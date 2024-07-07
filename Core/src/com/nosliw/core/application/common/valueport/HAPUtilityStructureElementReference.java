package com.nosliw.core.application.common.valueport;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityStructureElementReference {

	public static HAPInfoElementResolve resolveNameFromInternal(String name, String ioDirection, HAPConfigureResolveElementReference resolveConfigure, HAPWithInternalValuePort withValuePort) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildInternalElementReference(name, ioDirection, withValuePort); 
		return HAPUtilityStructureElementReference.resolveElementReferenceInternal(ref, null, withValuePort);
	}
	
	public static HAPInfoElementResolve resolveNameFromExternal(String name, String ioDirection, HAPConfigureResolveElementReference resolveConfigure, HAPWithExternalValuePort withValuePort) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildExternalElementReference(name, ioDirection, withValuePort); 
		return HAPUtilityStructureElementReference.resolveElementReferenceExternal(ref, null, withValuePort);
	}
	
	private static HAPReferenceElement buildInternalElementReference(String name, String ioDirection, HAPWithInternalValuePort withValuePort) {
		HAPReferenceElement ref = new HAPReferenceElement();
		ref.buildObject(name, HAPSerializationFormat.JSON);
		ref.setValuePortId(HAPUtilityValuePort.normalizeInternalValuePortId(ref.getValuePortId(), ioDirection, withValuePort));
		return ref;
	}
	
	private static HAPReferenceElement buildExternalElementReference(String name, String ioDirection, HAPWithExternalValuePort withValuePort) {
		HAPReferenceElement ref = new HAPReferenceElement();
		ref.buildObject(name, HAPSerializationFormat.JSON);
		ref.setValuePortId(HAPUtilityValuePort.normalizeExternalValuePortId(ref.getValuePortId(), ioDirection, withValuePort));
		return ref;
	}
	

	public static HAPIdRootElement resolveRootReferenceInBundle(HAPReferenceRootElement rootEleCriteria, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo){
		HAPResultReferenceResolve resolve = analyzeElementReferenceInBundle(new HAPReferenceElement(rootEleCriteria), resolveConfigure, bundle, resourceMan, runtimeInfo);
		return new HAPIdRootElement(rootEleCriteria.getValuePortId(), resolve.structureId, rootEleCriteria.getRootName());
	}


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
	
	
	public static HAPInfoElementResolve resolveElementReferenceInternal(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPWithInternalValuePort withValuePort) {
		HAPResultReferenceResolve refResolve = analyzeElementReferenceInternal(reference, resolveConfigure, withValuePort);
		return buildElementInfo(refResolve);
	}
	
	public static HAPInfoElementResolve resolveElementReferenceExternal(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPWithExternalValuePort withValuePort) {
		HAPResultReferenceResolve refResolve = analyzeElementReferenceExternal(reference, resolveConfigure, withValuePort);
		return buildElementInfo(refResolve);
	}
	
	public static HAPInfoElementResolve resolveElementReferenceInBundle(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo){
		HAPResultReferenceResolve refResolve = analyzeElementReferenceInBundle(reference, resolveConfigure, bundle, resourceMan, runtimeInfo);
		return buildElementInfo(refResolve);
	}

	private static HAPInfoElementResolve buildElementInfo(HAPResultReferenceResolve refResolve) {
		return new HAPInfoElementResolve(resolveToElementId(refResolve), refResolve.elementInfoSolid.resolvedElement);
	}
	
	private static HAPIdElement resolveToElementId(HAPResultReferenceResolve refResolve) {
		if(refResolve==null) {
			return null;
		}
		HAPIdRootElement rootEleId = new HAPIdRootElement(new HAPIdValuePortInBundle(refResolve.brickId, refResolve.valuePortId), refResolve.structureId, refResolve.rootName);
		return new HAPIdElement(rootEleId, new HAPComplexPath(refResolve.elementPath).getPathStr());
	}
	
	public static HAPResultReferenceResolve analyzeElementReferenceInBundle(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePortInBundle(reference.getValuePortId(), bundle, resourceMan, runtimeInfo);
		HAPResultReferenceResolve resolve = analyzeElementReferenceValuePort(reference, resolveConfigure, valuePort);
		if(resolve!=null) {
			resolve.brickId = reference.getValuePortId().getBrickId();
		}
		return resolve;
	}

	public static HAPResultReferenceResolve analyzeElementReferenceInternal(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPWithInternalValuePort withValuePort) {
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePortInternal(reference.getValuePortId(), withValuePort);
		return analyzeElementReferenceValuePort(reference, resolveConfigure, valuePort);
	}

	public static HAPResultReferenceResolve analyzeElementReferenceExternal(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPWithExternalValuePort withValuePort) {
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePortExternal(reference.getValuePortId(), withValuePort);
		return analyzeElementReferenceValuePort(reference, resolveConfigure, valuePort);
	}

	public static HAPResultReferenceResolve analyzeElementReferenceValuePort(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPValuePort valuePort) {
		HAPResultReferenceResolve resolve  = valuePort.resolveReference(reference, resolveConfigure);
		if(resolve!=null) {
			resolve.valuePortId = reference.getValuePortId().getValuePortId();
			resolve.elementPath = reference.getElementPath();
		}
		return resolve;
	}
	

	
/*	
	
	
	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(reference.getValuePortId(), processContext);
		List<HAPInfoValueStructureReference> valueStructureInfos = valuePort.discoverCandidateValueStructure(reference.getValueStructureReference());
		
		//resolve targeted structure element
		HAPResultReferenceResolve out =  analyzeElementReference(reference.getElementPath(), valueStructureInfos, resolveConfigure);
		if(out!=null) {
			out.eleReference = reference;
		}
		
		return out;
	}

	public static HAPIdRootElement resolveValueStructureRootReference1(HAPIdRootElement rootEleCriteria, HAPContextProcessor processContext){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(rootEleCriteria.getValuePortId(), processContext);
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
			HAPValueStructureInValuePort valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(simplePart.getRuntimeId());
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
