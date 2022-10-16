package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPEmbededWithIdDefinition getValueStructureComplexEntity();

	void setValueStructureComplexEntity(HAPEmbededWithIdDefinition valueStructureComplexEntity);
	
	String getValueStructureTypeIfNotDefined();

}
