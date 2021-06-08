package com.nosliw.data.core.common;

import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPUtilityWithValueStructure {

	public static HAPValueStructure getValueStructure(HAPWithValueStructure withValueStructure) {
		HAPWrapperValueStructure valueStructureWrapper = withValueStructure.getValueStructureWrapper();
		if(valueStructureWrapper==null)   return null;
		else return valueStructureWrapper.getValueStructure();
	}
	
	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPValueStructure valueStructure) {
		HAPWrapperValueStructure valueStructureWrapper = withValueStructure.getValueStructureWrapper();
		if(valueStructureWrapper==null) {
			withValueStructure.setValueStructureWrapper(new HAPWrapperValueStructure(valueStructure));
		}
		else{
			valueStructureWrapper.setValueStructure(valueStructure);
		}
		
	}
	
}
