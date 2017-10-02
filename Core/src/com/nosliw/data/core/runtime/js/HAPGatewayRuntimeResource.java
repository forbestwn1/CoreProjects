package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPGatewayRuntimeResource extends HAPRuntimeGatewayImp{

	@HAPAttribute
	final public static String REQUEST_DISCOVERRESOURCES = "requestDiscoverResources";
	@HAPAttribute
	final public static String REQUEST_DISCOVERRESOURCES_RESOURCEIDS = "resourceIds";

	@HAPAttribute
	final public static String REQUEST_DISCOVERANDLOADRESOURCES = "requestDiscoverAndLoadResources";
	@HAPAttribute
	final public static String REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS = "resourceIds";

	@HAPAttribute
	final public static String REQUEST_LOADRESOURCES = "requestLoadResources";
	@HAPAttribute
	final public static String REQUEST_LOADRESOURCES_RESOURCEINFOS = "resourceInfos";
	
	private HAPRuntimeEnvironment m_runtimeEnviroment;
	
	public HAPGatewayRuntimeResource(HAPRuntimeEnvironment runtimeEnviroment){
		this.m_runtimeEnviroment = runtimeEnviroment;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) {
		HAPServiceData out = null;
		try{
			switch(command){
			case REQUEST_DISCOVERRESOURCES:
				out = this.requestDiscoverResources(parms);
				break;
			case REQUEST_DISCOVERANDLOADRESOURCES:
				out = this.requestDiscoverAndLoadResources(parms);
				break;
			case REQUEST_LOADRESOURCES:
				out = this.requestLoadResources(parms);
				break;
			}
		}
		catch(Exception e){
			out = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * Callback method used to request to discover resources into runtime env
	 * @param objResourcesInfo: a list of resource id 
	 * @throws Exception 
	 * 
	 */
	private HAPServiceData requestDiscoverResources(JSONObject parms) throws Exception{
		JSONArray resourceJsonArray = parms.getJSONArray(REQUEST_DISCOVERRESOURCES_RESOURCEIDS);
		List<HAPResourceId> resourceIds = HAPSerializeUtility.buildListFromJsonArray(HAPResourceId.class.getName(), resourceJsonArray);
		return this.discoverResources(resourceIds);
	}
	
	/**
	 * Callback method used to request to load resources into runtime env
	 * @param objResourcesInfo: a list of resource info 
	 * @throws Exception 
	 */
	private HAPServiceData requestLoadResources(JSONObject parms) throws Exception{
		JSONArray resourceJsonArray = parms.getJSONArray(REQUEST_LOADRESOURCES_RESOURCEINFOS);
		List<HAPResourceInfo> resourcesInfo = HAPSerializeUtility.buildListFromJsonArray(HAPResourceInfo.class.getName(), resourceJsonArray);
		return this.loadResources(resourcesInfo);
	}
	
	/**
	 * Callback method used to request to discover resources and load into runtime env
	 * @param objResourcesInfo: a list of resource id 
	 * @throws Exception 
	 */
	private HAPServiceData requestDiscoverAndLoadResources(JSONObject parms) throws Exception{
		HAPServiceData serviceData = null;

		JSONArray resourceJsonArray = parms.getJSONArray(REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS);
		List<HAPResourceId> resourceIds = HAPSerializeUtility.buildListFromJsonArray(HAPResourceId.class.getName(), resourceJsonArray); 

		serviceData = this.discoverResources(resourceIds);
		if(serviceData.isFail())   return serviceData;
		else{
			List<HAPResourceInfo> resourceInfos = (List<HAPResourceInfo>)this.getSuccessData(serviceData);
			serviceData = this.loadResources(resourceInfos);
		}
		return serviceData;
	}
	
	private HAPServiceData discoverResources(List<HAPResourceId> resourceIds){
		List<HAPResourceInfo> resourceInfos = this.m_runtimeEnviroment.getResourceDiscovery().discoverResource(resourceIds);
		return this.createSuccessWithObject(resourceInfos);
	}
	
	private HAPServiceData loadResources(List<HAPResourceInfo> resourcesIdInfo){
		HAPServiceData serviceData = null;
		//load resources
		//Get all resource data
		//organize resource infos by id
		Map<HAPResourceId, HAPResourceInfo> resourcesInfo = new LinkedHashMap<HAPResourceId, HAPResourceInfo>();
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(HAPResourceInfo resourceInfo : resourcesIdInfo){ 
			resourceIds.add(resourceInfo.getId());  
			resourcesInfo.put(resourceInfo.getId(), resourceInfo);  
		}

		//Retrieve resources
		HAPLoadResourceResponse loadResourceResponse = this.m_runtimeEnviroment.getResourceManager().getResources(resourceIds);

		List<HAPResourceId> failedResourceIds = loadResourceResponse.getFailedResourcesId();
		if(failedResourceIds.size()==0){
			//if all loaded, build script, return null
			//build scripts info
			List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
			for(HAPResource resource : loadResourceResponse.getLoadedResources()){
				HAPResourceInfo resourceInfo = resourcesInfo.get(resource.getId());
				scriptsInfo.addAll(HAPRuntimeJSScriptUtility.buildScriptForResource(resourceInfo, resource));
			}
			serviceData = this.createSuccessWithScripts(scriptsInfo); 
		}
		else{
			//if some resources fail to load, then do not build script, just return response back
			String errorMsg = "Failed to load resources!!!";
			System.err.println(errorMsg);
			for(HAPResourceId resourceId : failedResourceIds){
				System.err.println(resourceId.toString());
			}
			serviceData = HAPServiceData.createFailureData(failedResourceIds, errorMsg);
		}
		return serviceData;
	}
	
}
