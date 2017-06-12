package com.nosliw.data.core.runtime.js.rhino;

import com.nosliw.data.core.runtime.js.HAPRuntimeGatewayJS;

public interface HAPRuntimeGatewayRhino extends HAPRuntimeGatewayJS{

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
	void expressionExecuteResult(String taskId, String result);
	
	/**
	 * Call back method used when all the resources are loaded, so that can execute expression
	 * @param taskId
	 */
	void resourcesLoaded(String taskId);
	
}
