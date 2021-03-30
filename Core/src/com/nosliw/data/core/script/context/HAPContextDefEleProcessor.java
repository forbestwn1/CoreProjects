package com.nosliw.data.core.script.context;

import org.apache.commons.lang3.tuple.Pair;

public interface HAPContextDefEleProcessor {

	//return true continue, false break
	//new element to replace old one
	Pair<Boolean, HAPContextDefinitionElement> process(HAPInfoContextNode eleInfo, Object value);

	void postProcess(HAPInfoContextNode eleInfo, Object value);
	
}
