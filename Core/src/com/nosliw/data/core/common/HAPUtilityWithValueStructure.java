package com.nosliw.data.core.common;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutablePartComplexValueStructureSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityWithValueStructure {

	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPValueStructure valueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addValueStructure(new HAPExecutablePartComplexValueStructureSimple(valueStructure, null));
	}
	
	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPDefinitionWrapperValueStructure valueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addValueStructure(new HAPExecutablePartComplexValueStructureSimple(valueStructure, null));
	}

	public static HAPValueStructure getValueStructure(HAPWithValueStructure withValueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		return ((HAPExecutablePartComplexValueStructureSimple)valueStructureComplex.getPart(null)).getValueStructureWrapper().getValueStructure();
	}
	
}
