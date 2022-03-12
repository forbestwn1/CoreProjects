package com.nosliw.data.core.common;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureGrouped;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityWithValueStructure {

	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPValueStructure valueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addPart(new HAPPartComplexValueStructureSimple(valueStructure, null));
	}
	
	public static void setValueStructure(HAPWithValueStructure withValueStructure, HAPValueStructureGrouped valueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		valueStructureComplex.addPart(new HAPPartComplexValueStructureSimple(valueStructure, null));
	}

	public static HAPValueStructure getValueStructure(HAPWithValueStructure withValueStructure) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = withValueStructure.getValueStructureComplex();
		return ((HAPPartComplexValueStructureSimple)valueStructureComplex.getPart(null)).getValueStructureWrapper().getValueStructure();
	}
	
}
