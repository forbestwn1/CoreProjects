package com.nosliw.core.application.common.structure;

import java.util.List;

public interface HAPValueContextDefinition{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructureDefinition> getValueStructures();
	
}
