package com.nosliw.core.application.common.structure;

import org.apache.commons.lang3.tuple.Pair;

public interface HAPProcessorStructureElement {

	//return true continue, false break
	//new element to replace old one
	Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value);

	void postProcess(HAPInfoElement eleInfo, Object value);
	
}
