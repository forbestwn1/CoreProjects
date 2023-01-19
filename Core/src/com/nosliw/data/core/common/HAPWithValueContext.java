package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithId;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";
	
	HAPEmbededDefinitionWithId getValueContextEntity();

	void setValueContextEntity(HAPEmbededDefinitionWithId valueStructureComplexEntity);
	
	String getValueStructureTypeIfNotDefined();

}
