package com.nosliw.core.application;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPStructureImp;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructure;

public class HAPUtilityValueStructureDomain {

	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPDomainValueStructure valueStructureDomain) {
		HAPStructureImp structureDef = valueStructureDomain.getStructureDefinitionByRuntimeId(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityStructure.getDescendant(structureDef.getRootByName(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}
	
	
}
