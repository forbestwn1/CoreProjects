package com.nosliw.data.core.structure.temp;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPInfoElement;

public interface HAPProcessorContextDefinitionElement {

	//return true continue, false break
	//new element to replace old one
	Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value);

	void postProcess(HAPInfoElement eleInfo, Object value);
	
}
