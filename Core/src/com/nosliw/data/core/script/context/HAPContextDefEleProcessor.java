package com.nosliw.data.core.script.context;

public interface HAPContextDefEleProcessor {

	//return true continue, false break
	boolean process(HAPInfoContextNode eleInfo, Object value);

	boolean postProcess(HAPInfoContextNode eleInfo, Object value);
	
}
