package com.nosliw.data.core.runtime.js.rhino;

import com.nosliw.data.core.runtime.js.HAPRuntimeGatewayJS;

public interface HAPRuntimeGatewayRhino extends HAPRuntimeGatewayJS{

	/**
	 * Callback method used to request to discover resources into runtime env
	 * @param objResourcesInfo: a list of resource id 
	 * @param callBackFunction (discovered resource info)
	 */
	void descoverResources(Object objResourceIds, Object callBackFunction);
	
	/**
	 * Callback method used to request to discover resources and load into runtime env
	 * @param objResourcesInfo: a list of resource id 
	 * @param callBackFunction (discovered and loaded resource info)
	 */
	void discoverAndLoadResources(Object objResourceIds, Object callBackFunction);
	
	/**
	 * Callback method used to request to load resources into runtime env
	 * @param objResourcesInfo: a list of resource info 
	 * @param callBackFunction (nothing)
	 */
	void loadResources(Object objResourcesInfo, Object callBackFunction);

	
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
