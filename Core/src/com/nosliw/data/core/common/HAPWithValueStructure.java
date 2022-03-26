package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededEntity;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPEmbededEntity getValueStructureComplexEntity();

	void setValueStructureComplexEntity(HAPEmbededEntity valueStructureComplexEntity);
	
	String getValueStructureTypeIfNotDefined();

}
