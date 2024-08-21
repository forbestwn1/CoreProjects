package com.nosliw.core.application.common.structure;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPInfo;

public interface HAPWrapperValueStructure extends HAPEntityInfo{

	public static final String NAME = "name";
	public static final String GROUPTYPE = "groupType";
	public static final String INFO = "info";
	public static final String VALUESTRUCTURE = "valueStructure";

	HAPInfo getInfo();
	void setInfo(HAPInfo info);
	
	HAPValueStructureDefinition getValueStructure();
	void setValueStructure(HAPValueStructureDefinition valueStructure);
	
	String getName();
	void setName(String name);
	
	String getGroupType();
	void setGroupType(String groupType);
	
}
