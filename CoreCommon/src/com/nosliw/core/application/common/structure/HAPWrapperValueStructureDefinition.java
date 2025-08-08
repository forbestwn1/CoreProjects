package com.nosliw.core.application.common.structure;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPWrapperValueStructureDefinition extends HAPEntityInfo{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	public static final String VALUESTRUCTUREINFO = "valueStructureInfo";

	HAPValueStructure getValueStructure();
	void setValueStructure(HAPValueStructure valueStructure);

	HAPInfoStructureInWrapper getStructureInfo();
	void setStructureInfo(HAPInfoStructureInWrapper info);
	
}
