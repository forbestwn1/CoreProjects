package com.nosliw.core.application.common.valueport;

import com.nosliw.core.application.common.structure.HAPElementStructure;

public interface HAPValuePort{

	HAPInfoValuePort getValuePortInfo();
	
	HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure);
	
	HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId);

	void updateElement(HAPIdElement elementId, HAPElementStructure structureElement);
	
}
