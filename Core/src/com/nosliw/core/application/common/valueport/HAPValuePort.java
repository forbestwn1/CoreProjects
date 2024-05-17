package com.nosliw.core.application.common.valueport;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPValuePort extends HAPEntityInfo{

	HAPInfoValuePort getValuePortInfo();
	
	HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure);
	
	HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId);
	
}
