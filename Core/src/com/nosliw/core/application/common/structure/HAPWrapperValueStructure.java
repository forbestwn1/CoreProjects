package com.nosliw.core.application.common.structure;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPWrapperValueStructure extends HAPEntityInfo{

	public static final String GROUPTYPE = "groupType";
	public static final String VALUESTRUCTURE = "valueStructure";

	HAPValueStructureDefinition getValueStructure();
	void setValueStructure(HAPValueStructureDefinition valueStructure);

	String getGroupType();
	void setGroupType(String groupType);
	
}
