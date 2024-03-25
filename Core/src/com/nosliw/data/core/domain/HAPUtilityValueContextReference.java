package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;

public class HAPUtilityValueContextReference {

	//resolve variable name with possible extension
	public static HAPIdVariable resolveVariableName(String variableName, HAPExecutableEntityValueContext valueContext, String extensionStructureGroup, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveElementReference resolveConfigure){
		HAPIdVariable out = HAPUtilityValueContextReference.resolveVariableReference(new HAPReferenceElement(variableName), null, valueContext, valueStructureDomain, resolveConfigure);
		if(out==null) {
			//not able to resolve variable
			String valueStructureRuntimId = HAPUtilityValueContext.getExtensionValueStructure(valueContext, extensionStructureGroup!=null?extensionStructureGroup:HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			if(valueStructureRuntimId!=null) {
				HAPManualBrickValueStructure vs = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureRuntimId);
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
	
	public static HAPIdVariable resolveVariableReference(HAPReferenceElement reference, Set<String> groupType, HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveElementReference resolveConfigure){
		
		HAPResultReferenceResolve refResolve = resolveElementReference(reference, groupType, valueContext, valueStructureDomain, resolveConfigure);
		
		if(refResolve==null)  return null;
		
		HAPIdRootElement rootEleId = new HAPIdRootElement(null, refResolve.structureId, refResolve.rootName);
		
		return new HAPIdVariable(rootEleId, reference.getLeafPath());
	}

	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, Set<String> groupType, HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveElementReference resolveConfigure){

		HAPContextStructureReferenceValueStructure structureRefContext = new HAPContextStructureReferenceValueStructure(valueContext, groupType, valueStructureDomain);

		return HAPUtilityStructureElementReference.resolveElementReference(reference, structureRefContext, resolveConfigure);
		
	}	

}
