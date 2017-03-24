package com.nosliw.data.core.runtime;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.HAPInfo;

/**
 * This interface represent a resource 
 * Every resource have 
 * 		a global unique id
 *  	type of resource
 *  	status of resource: available, not available
 */
public class HAPResource{

	private HAPResourceId m_id;
	
	private Object m_resourceData;
	
	private HAPInfo m_info;
	
	private Set<HAPResourceId> m_dependency = new HashSet<HAPResourceId>();
	
	public HAPResource(HAPResourceId id, Object resourceData, HAPInfo info){
		this.m_id = id;
		this.m_resourceData = resourceData;
		this.m_info = info;
	}
	
	public HAPResourceId getId(){		return this.m_id;	}
	
	public HAPInfo getInfo(){  return this.m_info;  }
	
	public Object getResourceData(){  return this.m_resourceData;  }
	
	public Set<HAPResourceId> getDependentResources(){  return this.m_dependency;  }
	public void addDependendcy(HAPResourceId resourceId){  this.m_dependency.add(resourceId); }
}
