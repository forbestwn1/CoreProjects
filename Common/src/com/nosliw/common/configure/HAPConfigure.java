package com.nosliw.common.configure;

/*
 * store configure items 
 * value for configure item can be retrieved as string, boolean, integer, float, array and HAPConfigurable itself 
 */
public interface HAPConfigure {

	public HAPConfigureValue getConfigureValue(String attr);  
	public HAPConfigure cloneChildConfigure(String name);
}
