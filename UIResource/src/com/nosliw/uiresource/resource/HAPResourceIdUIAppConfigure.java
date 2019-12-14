package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIAppConfigure  extends HAPResourceId{

	public HAPResourceIdUIAppConfigure(){}

	public HAPResourceIdUIAppConfigure(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIAppConfigure(String id){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE, id, null);
	}

	@Override
	public HAPResourceIdUIAppConfigure clone(){
		HAPResourceIdUIAppConfigure out = new HAPResourceIdUIAppConfigure();
		out.cloneFrom(this);
		return out;
	}
}
