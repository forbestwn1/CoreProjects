package com.nosliw.data;

import com.nosliw.common.serialization.HAPSerializable;

/**
 * This interface represent a resource 
 * Every resource have 
 * 		a global unique id
 *  	type of resource
 *  	status of resource: available, not available
 * @author ewaniwa
 *
 */
public interface HAPResource extends HAPSerializable{

	public String getId();
	
	public String getType();
	
	public String getStatus();
	
}
