package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDiscovered;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

@HAPEntityWithAttribute
public class HAPGatewayServlet extends HAPServiceServlet{

	private static final long serialVersionUID = 3449216679929442927L;

	@HAPAttribute
	public static final String REQUEST_LOADRESOURCES = "requestLoadResources";
	@HAPAttribute
	public static final String REQUEST_LOADRESOURCES_RESOURCEINFOS = "resourceInfos";
	@HAPAttribute
	public static final String REQUEST_DISCOVERRESOURCES = "requestDiscoverResources";
	@HAPAttribute
	public static final String REQUEST_DISCOVERRESOURCES_RESOURCEIDS = "resourceIds";
	@HAPAttribute
	public static final String REQUEST_DISCOVERANDLOADRESOURCES = "requestDiscoverAndLoadResources";
	@HAPAttribute
	public static final String REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS = "resourceIds";
	
	
	@Override
	protected HAPServiceData processServiceRequest(String command, Map<String, Object> parms) {
		HAPServiceData out = null;
		switch(command){
		case REQUEST_LOADRESOURCES:
		{
			JSONArray resourceInfosArray = (JSONArray)parms.get(REQUEST_LOADRESOURCES_RESOURCEINFOS);
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			for(int i=0; i<resourceInfosArray.length(); i++){
				HAPResourceInfo resourceInfo = new HAPResourceInfo();
				resourceInfo.buildObject(resourceInfosArray.opt(i), HAPSerializationFormat.JSON);
				resourceIds.add(resourceInfo.getId());
			}
			HAPLoadResourceResponse loadResourceResponse = this.getRuntime().getResourceManager().getResources(resourceIds);
			if(loadResourceResponse.isSuccess())	out = HAPServiceData.createSuccessData(loadResourceResponse.getLoadedResources());
			else	out = HAPServiceData.createFailureData(loadResourceResponse.getFailedResourcesId(), "");
			break;
		}	
		case REQUEST_DISCOVERRESOURCES:
		{
			JSONArray resourceIdsArray = (JSONArray)parms.get(REQUEST_DISCOVERRESOURCES_RESOURCEIDS);
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			for(int i=0; i<resourceIdsArray.length(); i++){
				HAPResourceId resourceId = new HAPResourceId();
				resourceId.buildObject(resourceIdsArray.opt(i), HAPSerializationFormat.JSON);
				resourceIds.add(resourceId);
			}
			List<HAPResourceInfo> resourceInfos = this.getRuntime().getResourceDiscovery().discoverResource(resourceIds);
			out = HAPServiceData.createSuccessData(resourceInfos);
			break;
		}
		case REQUEST_DISCOVERANDLOADRESOURCES:
		{
			JSONArray resourceIdsArray = (JSONArray)parms.get(REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS);
			List<HAPResourceId> discoveryResourceIds = new ArrayList<HAPResourceId>();
			for(int i=0; i<resourceIdsArray.length(); i++){
				HAPResourceId resourceId = new HAPResourceId();
				resourceId.buildObject(resourceIdsArray.opt(i), HAPSerializationFormat.JSON);
				discoveryResourceIds.add(resourceId);
			}
			List<HAPResourceInfo> resourceInfos = this.getRuntime().getResourceDiscovery().discoverResource(discoveryResourceIds);

			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			Map<HAPResourceId, HAPResourceInfo> resourceInfoMap = new LinkedHashMap<HAPResourceId, HAPResourceInfo>();
			for(HAPResourceInfo resourceInfo : resourceInfos){  
				resourceIds.add(resourceInfo.getId());  
				resourceInfoMap.put(resourceInfo.getId(), resourceInfo);
			} 

			HAPLoadResourceResponse loadResourceResponse = this.getRuntime().getResourceManager().getResources(resourceIds);
			List<HAPResourceDiscovered> discoveredResource = new ArrayList<HAPResourceDiscovered>();
			if(loadResourceResponse.isSuccess()){
				for(HAPResource resource : loadResourceResponse.getLoadedResources())		discoveredResource.add(new HAPResourceDiscovered(resourceInfoMap.get(resource.getId()), resource));
				out = HAPServiceData.createSuccessData(discoveredResource);
			}
			else{
				out = HAPServiceData.createFailureData(loadResourceResponse.getFailedResourcesId(), "");
			}
			break;
		}
		}
		
		return out;
	}

	private HAPRuntimeImpJSBroswer getRuntime(){		return (HAPRuntimeImpJSBroswer)this.getServletContext().getAttribute("runtime");  }
	
}
