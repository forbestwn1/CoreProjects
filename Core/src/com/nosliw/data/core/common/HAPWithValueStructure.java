package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPValueStructure getValueStructure();
	void setValueStructure(HAPValueStructure valueStructure);

	void cloneToWithValueStructure(HAPWithValueStructure dataContext);
}
