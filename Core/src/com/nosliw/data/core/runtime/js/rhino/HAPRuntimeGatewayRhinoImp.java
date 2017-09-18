package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPRuntimeGatewayRhinoImp implements HAPRuntimeGatewayRhino{

	private HAPRuntimeEnvironment m_runtimeEnviroment;
	
	private HAPRuntimeImpRhino m_runtime;
	
	//scope
	private Scriptable m_scope;

	public HAPRuntimeGatewayRhinoImp(HAPRuntimeEnvironment runtimeEnviroment, HAPRuntimeImpRhino runtime, Scriptable scope){
		this.m_runtimeEnviroment = runtimeEnviroment;
		this.m_runtime = runtime;
		this.m_scope = scope;
	}
	
	//gateway callback method
	@Override
	public void requestDiscoverResources(Object objResourceIds, Object handlers){
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds); 
			List<HAPResourceInfo> resourceInfos = this.m_runtimeEnviroment.getResourceDiscovery().discoverResource(resourceIds);
			HAPServiceData serviceData = HAPServiceData.createSuccessData(resourceInfos);
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void requestDiscoverAndLoadResources(Object objResourceIds, Object handlers){
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds);
			//discovery
			List<HAPResourceInfo> resourceInfos = this.m_runtimeEnviroment.getResourceDiscovery().discoverResource(resourceIds);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.m_runtime.loadResources(resourceInfos, m_scope);

			HAPServiceData serviceData = null;
			if(response.isSuccess()){
				serviceData = HAPServiceData.createSuccessData(resourceInfos);
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "");
			}
			
			//callback with resourceInfos
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void requestLoadResources(Object objResourcesInfo, Object handlers){
		try{
			List<HAPResourceInfo> resourcesInfo = HAPRhinoRuntimeUtility.rhinoResourcesInfoToResourcesInfo((NativeArray)objResourcesInfo);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.m_runtime.loadResources(resourcesInfo, m_scope);
			HAPServiceData serviceData = null;
			if(response==null){
				serviceData = HAPServiceData.createSuccessData();
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "");
			}
			HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//gateway callback method
	@Override
	public void notifyExpressionExecuteResult(String taskId, Object result){
		HAPServiceData taskServiceData;
		try{
			String resultStr = HAPRhinoDataUtility.toJson(result).toString();
			HAPDataWrapper resultData = new HAPDataWrapper(resultStr); 
			taskServiceData = HAPServiceData.createSuccessData(resultData);
		}
		catch(Exception e){
			taskServiceData = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		this.m_runtime.finishTask(taskId, taskServiceData);
	}

	//gateway callback method
	@Override
	public void notifyScriptExpressionExecuteResult(String taskId, Object result){
		HAPServiceData taskServiceData;
		try{
			Object resultObj = HAPRhinoDataUtility.toJson(result);
			taskServiceData = HAPServiceData.createSuccessData(resultObj);
		}
		catch(Exception e){
			taskServiceData = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		this.m_runtime.finishTask(taskId, taskServiceData);
	}

	
	//gatewary callback method
	@Override
	public void notifyResourcesLoaded(String taskId){
		HAPServiceData taskServiceData;
		try{
			taskServiceData = HAPServiceData.createSuccessData();
		}
		catch(Exception e){
			taskServiceData = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		this.m_runtime.finishTask(taskId, taskServiceData);
	}
}
