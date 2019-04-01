package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HAPLoadResourceResponse {

	private List<HAPResource> m_loaded;
	private Map<String, HAPResource> m_loadedMap;
	
	private List<HAPResourceId> m_failed;
	
	public HAPLoadResourceResponse(){
		this.m_loaded = new ArrayList<HAPResource>();
		this.m_failed = new ArrayList<HAPResourceId>();
		this.m_loadedMap = new LinkedHashMap<String, HAPResource>();
	}
	
	public void addLoadedResource(HAPResource resource){
		this.m_loaded.add(resource);
		this.m_loadedMap.put(resource.getId().getId(), resource);
	}
	
	public HAPResource getLoadedResource(HAPResourceId resourceId){
		return this.m_loadedMap.get(resourceId.getId());
	}
	
	public void addFaildResourceId(HAPResourceId resourceId){  
		this.m_failed.add(resourceId);   
	}
	
	public List<HAPResourceId> getFailedResourcesId(){ return this.m_failed; }
	
	public List<HAPResource> getLoadedResources(){  return this.m_loaded;  }
	
	public boolean isSuccess(){		return this.m_failed==null || this.m_failed.size()==0;	}
}
