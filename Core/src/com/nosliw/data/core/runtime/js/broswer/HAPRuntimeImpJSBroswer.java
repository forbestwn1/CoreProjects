package com.nosliw.data.core.runtime.js.broswer;

import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeGatewayRhino;

public class HAPRuntimeImpJSBroswer extends HAPRuntimeImpJS implements HAPRuntimeGatewayRhino{

	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeExpressionTask(HAPExecuteExpressionTask result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceManager getResourceManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDiscoverResources(Object objResourceIds, Object callBackFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDiscoverAndLoadResources(Object objResourceIds, Object callBackFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestLoadResources(Object objResourcesInfo, Object callBackFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyExpressionExecuteResult(String taskId, Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyResourcesLoaded(String taskId) {
		// TODO Auto-generated method stub
		
	}

}
