package com.nosliw.data.core.script.context;

public interface HAPContextDefEleProcessor {

	//return true continue, false break
	boolean process(HAPContextDefinitionElement ele, Object value);

	boolean postProcess(HAPContextDefinitionElement ele, Object value);
	
}
