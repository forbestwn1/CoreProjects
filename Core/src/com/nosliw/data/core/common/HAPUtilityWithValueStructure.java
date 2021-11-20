package com.nosliw.data.core.common;

import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityWithValueStructure {

	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPValueStructure valueStructure) {
		HAPComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addPart(new HAPPartComplexValueStructureSimple(valueStructure, null));
	}
	
	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPWrapperValueStructure valueStructure) {
		HAPComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addPart(new HAPPartComplexValueStructureSimple(valueStructure, null));
	}

	public static HAPValueStructure getValueStructure(HAPWithValueStructure withValueStructure) {
		HAPComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		return ((HAPPartComplexValueStructureSimple)valueStructureComplex.getPart(null)).getValueStructureWrapper().getValueStructure();
	}
	
}
