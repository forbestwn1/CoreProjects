package com.nosliw.core.application.common.valueport;

public interface HAPValuePort{

	HAPInfoValuePort getValuePortInfo();
	
	HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure);
	
	HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId);
	
}
