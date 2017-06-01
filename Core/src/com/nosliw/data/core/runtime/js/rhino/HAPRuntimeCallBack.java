package com.nosliw.data.core.runtime.js.rhino;

/**
 * this interface define all the call back method that invoked from runtime env (rhino) 
 */
public interface HAPRuntimeCallBack {

	/**
	 * Callback method used to request to load resources into runtime env
	 * @param resources
	 * @param callBackFunction
	 */
	void loadResources(Object resources, Object callBackFunction);
	
	/**
	 * Callback method used to return expression result to runtime env
	 * @param expressionId: expression id executed
	 * @param result  the data result
	 */
	void returnResult(String expressionId, String result);
	
}
