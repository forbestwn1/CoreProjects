package com.nosliw.core.xxx.application.valueport;

import com.nosliw.core.application.common.structure.HAPElementStructure;

public interface HAPValuePort1111{

	HAPInfoValuePort getValuePortInfo();
	
	HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure);
	
	HAPValueStructureInValuePort11111 getValueStructureDefintion(String valueStructureId);

	void updateElement(HAPIdElement elementId, HAPElementStructure structureElement);
	
}
