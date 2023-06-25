package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPUtilityValueContextReference {

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
