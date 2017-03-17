package com.nosliw.data.core.resource;

import java.util.List;

import com.nosliw.data.core.HAPInfo;

/**
 * This interface represent a resource 
 * Every resource have 
 * 		a global unique id
 *  	type of resource
 *  	status of resource: available, not available
 */
public interface HAPResource{

	HAPResourceId getId();
	
	HAPInfo getInfo();
	
	Object getResourceData();
	
	List<HAPResourceId> getDependentResources();
}
