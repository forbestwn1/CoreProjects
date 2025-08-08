package com.nosliw.core.xxx.application.common.structure;

import java.util.List;

import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;

public interface HAPValueContextDefinition{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructureDefinition> getValueStructures();
	
}
