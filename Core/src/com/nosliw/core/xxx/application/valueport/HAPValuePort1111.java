package com.nosliw.core.xxx.application.valueport;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;

public interface HAPValuePort1111{

	HAPInfoValuePort getValuePortInfo();
	
	HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure);
	
	HAPValueStructureInValuePort11111 getValueStructureDefintion(String valueStructureId);

	void updateElement(HAPIdElement elementId, HAPElementStructure structureElement);
	
}
