package com.nosliw.core.application;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.valueport.HAPIdElement;

public class HAPUtilityValueStructureDomain {

	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPDomainValueStructure valueStructureDomain) {
		HAPStructure structureDef = valueStructureDomain.getStructureDefinitionByRuntimeId(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityElement.getDescendant(structureDef.getRootByName(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}
	
	
}
