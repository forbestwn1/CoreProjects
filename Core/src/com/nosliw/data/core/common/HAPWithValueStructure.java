package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPIdEntityInDomain getValueStructureComplexId();

	void setValueStructureComplexId(HAPIdEntityInDomain valueStructureComplexId);
	
	String getValueStructureTypeIfNotDefined();

}
