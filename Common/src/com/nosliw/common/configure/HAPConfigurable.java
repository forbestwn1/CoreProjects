package com.nosliw.common.configure;

/*
 * all entity that can be configuable 
 */
public interface HAPConfigurable {
	
	public HAPConfigureValue getConfigureValue(String attr);  
	
	public HAPConfigureImp getConfiguration();
	
}
