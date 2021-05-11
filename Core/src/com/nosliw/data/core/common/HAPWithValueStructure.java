package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPValueStructureDefinition getValueStructure();
	void setValueStructure(HAPValueStructureDefinition valueStructure);

	void cloneToWithValueStructure(HAPWithValueStructure dataContext);
}
