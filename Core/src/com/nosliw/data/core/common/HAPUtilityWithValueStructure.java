package com.nosliw.data.core.common;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutablePartValueContextSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityWithValueStructure {

	public static void setValueStructure(HAPWithValueContext withValueStructure, HAPValueStructure valueStructure) {
		HAPDefinitionEntityValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addValueStructure(new HAPExecutablePartValueContextSimple(valueStructure, null));
	}
	
	public static void setValueStructure(HAPWithValueContext withValueStructure, HAPDefinitionWrapperValueStructure valueStructure) {
		HAPDefinitionEntityValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addValueStructure(new HAPExecutablePartValueContextSimple(valueStructure, null));
	}

	public static HAPValueStructure getValueStructure(HAPWithValueContext withValueStructure) {
		HAPDefinitionEntityValueContext valueStructureComplex = withValueStructure.getValueContext();
		return ((HAPExecutablePartValueContextSimple)valueStructureComplex.getPart(null)).getValueStructureWrapper().getValueStructure();
	}
	
}
