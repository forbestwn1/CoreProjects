package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import org.mozilla.javascript.Context;
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
		HAPServiceData serviceData;
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds); 
			List<HAPResourceInfo> resourceInfos = this.m_runtimeEnviroment.getResourceDiscovery().discoverResource(resourceIds);
			serviceData = HAPServiceData.createSuccessData(resourceInfos);
		}
		catch(Exception e){
			serviceData = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
	}
	
	//gateway callback method
	@Override
	public void requestDiscoverAndLoadResources(Object objResourceIds, Object handlers){
		HAPServiceData serviceData = null;
		try{
			List<HAPResourceId> resourceIds = HAPRhinoRuntimeUtility.rhinoResourcesIdToResourcesId((NativeArray)objResourceIds);
			//discovery
			List<HAPResourceInfo> resourceInfos = this.m_runtimeEnviroment.getResourceDiscovery().discoverResource(resourceIds);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.m_runtime.loadResources(resourceInfos, m_scope);

			if(response.isSuccess()){
				serviceData = HAPServiceData.createSuccessData(resourceInfos);
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "");
			}
		}
		catch(Exception e){
			serviceData = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		//callback with resourceInfos
		HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
	}
	
	//gateway callback method
	@Override
	public void requestLoadResources(Object objResourcesInfo, Object handlers){
		HAPServiceData serviceData = null;
		try{
			List<HAPResourceInfo> resourcesInfo = HAPRhinoRuntimeUtility.rhinoResourcesInfoToResourcesInfo((NativeArray)objResourcesInfo);
			//load resources to rhino runtime
			HAPLoadResourceResponse response = this.m_runtime.loadResources(resourcesInfo, m_scope);
			if(response==null){
				serviceData = HAPServiceData.createSuccessData();
			}
			else{
				serviceData = HAPServiceData.createFailureData(response.getFailedResourcesId(), "Fail to load resources");
			}
		}
		catch(Exception e){
			serviceData = HAPServiceData.createFailureData(e, "Exception during loading resources");
			e.printStackTrace();
		}
		HAPRhinoRuntimeUtility.invokeGatewayHandlers(serviceData, handlers, m_scope);
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
	public void notifyResourcesLoaded(String taskId, Object serviceDataObj){
		Context context = Context.enter();
		try{
			HAPServiceData serviceData = null;
			if(serviceDataObj==null){
				//if success
				serviceData = HAPServiceData.createSuccessData();
			}
			else{
				serviceData = (HAPServiceData)context.jsToJava(serviceDataObj, HAPServiceData.class); 
			}
			this.m_runtime.finishTask(taskId, serviceData);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
}
