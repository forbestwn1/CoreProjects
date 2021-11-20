package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valuestructure";
	
	HAPComplexValueStructure getValueStructureComplex();

	String getValueStructureTypeIfNotDefined();
}
