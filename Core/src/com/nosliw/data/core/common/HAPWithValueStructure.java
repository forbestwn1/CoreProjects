package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithId;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPEmbededDefinitionWithId getValueStructureComplexEntity();

	void setValueStructureComplexEntity(HAPEmbededDefinitionWithId valueStructureComplexEntity);
	
	String getValueStructureTypeIfNotDefined();

}
