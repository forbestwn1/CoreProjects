package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPContextStructureReferenceValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.domain.valueport.HAPReferenceElementInValueStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPUtilityValueContextReference {

	//resolve variable name with possible extension
	public static HAPIdVariable resolveVariableName(String variableName, HAPExecutableEntityValueContext valueContext, String extensionStructureGroup, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure){
		HAPIdVariable out = HAPUtilityValueContextReference.resolveVariableReference(new HAPReferenceElementInValueStructure(variableName), null, valueContext, valueStructureDomain, resolveConfigure);
		if(out==null) {
			//not able to resolve variable
			String valueStructureRuntimId = HAPUtilityValueContext.getExtensionValueStructure(valueContext, extensionStructureGroup!=null?extensionStructureGroup:HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			if(valueStructureRuntimId!=null) {
				HAPDefinitionEntityValueStructure vs = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureRuntimId);
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
	
	public static HAPIdVariable resolveVariableReference(HAPReferenceElementInValueStructure reference, Set<String> groupType, HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure){
		
		HAPInfoReferenceResolve refResolve = resolveElementReference(reference, groupType, valueContext, valueStructureDomain, resolveConfigure);
		
		if(refResolve==null)  return null;
		
		HAPIdRootElement rootEleId = new HAPIdRootElement(null, refResolve.structureId, refResolve.rootName);
		
		return new HAPIdVariable(rootEleId, reference.getLeafPath());
	}

	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElementInValueStructure reference, Set<String> groupType, HAPExecutableEntityValueContext valueContext, HAPDomainValueStructure valueStructureDomain, HAPConfigureResolveStructureElementReference resolveConfigure){

		HAPContextStructureReferenceValueStructure structureRefContext = new HAPContextStructureReferenceValueStructure(valueContext, groupType, valueStructureDomain);

		return HAPUtilityStructureElementReference.resolveElementReference(reference, structureRefContext, resolveConfigure);
		
	}	

}
