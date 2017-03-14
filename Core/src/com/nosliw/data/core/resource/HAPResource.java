package com.nosliw.data.core.resource;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.HAPInfo;

/**
 * This interface represent a resource 
 * Every resource have 
 * 		a global unique id
 *  	type of resource
 *  	status of resource: available, not available
 */
public interface HAPResource extends HAPSerializable{

	HAPResourceId getId();
	
	HAPInfo getInfo();
	
	Object getResource();
}
