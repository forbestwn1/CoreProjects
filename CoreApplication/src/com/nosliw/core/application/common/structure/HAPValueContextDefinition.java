package com.nosliw.core.application.common.structure;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPValueContextDefinition extends HAPSerializable{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructureDefinition> getValueStructures();
	
}
