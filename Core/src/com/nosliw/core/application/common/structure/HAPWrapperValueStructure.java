package com.nosliw.core.application.common.structure;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPWrapperValueStructure extends HAPEntityInfo{

	public static final String GROUPTYPE = "groupType";
	public static final String VALUESTRUCTURE = "valueStructure";
	public static final String INHERITMODE = "inheritMode";

	HAPValueStructure getValueStructure();
	void setValueStructure(HAPValueStructure valueStructure);

	String getGroupType();
	void setGroupType(String groupType);
	
	String getInheritMode();
	void setInheritMode(String mode);
	
}
