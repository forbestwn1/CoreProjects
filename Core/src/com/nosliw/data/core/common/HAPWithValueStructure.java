package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public interface HAPWithValueStructure {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	HAPWrapperValueStructure getValueStructureWrapper();
	void setValueStructureWrapper(HAPWrapperValueStructure valueStructureWrapper);

}
