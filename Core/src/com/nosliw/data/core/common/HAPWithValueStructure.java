package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededWithId;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPEmbededWithId getValueStructureComplexEntity();

	void setValueStructureComplexEntity(HAPEmbededWithId valueStructureComplexEntity);
	
	String getValueStructureTypeIfNotDefined();

}
