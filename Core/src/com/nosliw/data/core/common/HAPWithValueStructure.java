package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPStructureValueDefinition getValueStructure();
	void setValueStructure(HAPStructureValueDefinition valueStructure);

	void cloneToWithValueStructure(HAPWithValueStructure dataContext);
}
