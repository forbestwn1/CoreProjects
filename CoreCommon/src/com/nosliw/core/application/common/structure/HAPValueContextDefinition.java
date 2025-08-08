package com.nosliw.core.application.common.structure;

import java.util.List;

import com.nosliw.core.application.common.structure22.HAPWrapperValueStructureDefinition;

public interface HAPValueContextDefinition{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructureDefinition> getValueStructures();
	
}
