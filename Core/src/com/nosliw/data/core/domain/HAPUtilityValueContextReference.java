package com.nosliw.data.core.domain;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPUtilityValueContextReference {

	//resolve variable name with possible extension
	public static HAPIdVariable resolveVariableName(String variableName, HAPIdEntityInDomain complexEntityExeId, String extensionStructureGroup, HAPContextProcessor processorContext, HAPConfigureResolveStructureElementReference resolveConfigure){
		HAPIdVariable out = HAPUtilityValueContextReference.resolveVariableReference(new HAPReferenceElementInValueContext(variableName), complexEntityExeId, processorContext, resolveConfigure);
		if(out==null) {
			//not able to resolve variable
			HAPExecutableEntityComplex executableEntity = processorContext.getCurrentBundle().getExecutableDomain().getEntityInfoExecutable(complexEntityExeId).getEntity();
			String valueStructureRuntimId = HAPUtilityValueContext.getExtensionValueStructure(executableEntity.getValueContext(), extensionStructureGroup!=null?extensionStructureGroup:HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			if(valueStructureRuntimId!=null) {
				HAPDefinitionEntityValueStructure vs = processorContext.getCurrentValueStructureDomain().getValueStructureDefinitionByRuntimeId(valueStructureRuntimId);
				HAPComplexPath varPath = new HAPComplexPath(variableName);
				HAPUtilityStructure.setDescendant(vs, varPath, new HAPElementStructureLeafData());
				out = new HAPIdVariable(new HAPIdRootElement(valueStructureRuntimId, varPath.getRoot()), varPath.getPath().toString());
			}
			else {
				throw new RuntimeException();
			}
		}
		return out;
	}
	
	public static HAPIdVariable resolveVariableReference(HAPReferenceElementInValueContext reference, HAPIdEntityInDomain complexEntityExeId, HAPContextProcessor processorContext, HAPConfigureResolveStructureElementReference resolveConfigure){
		
		HAPInfoReferenceResolve refResolve = resolveElementReference(reference, complexEntityExeId, processorContext, resolveConfigure);
		
		if(refResolve==null)  return null;
		
		HAPIdRootElement rootEleId = new HAPIdRootElement(null, refResolve.structureId, refResolve.rootName);
		
		return new HAPIdVariable(rootEleId, reference.getElementPath());
	}

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueContext reference, HAPIdEntityInDomain complexEntityExeId, HAPContextProcessor processorContext, HAPConfigureResolveStructureElementReference resolveConfigure){

		HAPContextStructureReferenceValueStructure structureRefContext = new HAPContextStructureReferenceValueStructure(processorContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExeId).getEntity().getValueContext(), null, processorContext.getCurrentValueStructureDomain());

		return HAPUtilityStructureElementReference.resolveElementReference(reference, structureRefContext, resolveConfigure);
		
	}	

}
