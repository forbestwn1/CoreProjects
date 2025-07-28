package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIAppConfigure  extends HAPResourceIdSimple{

	public HAPResourceIdUIAppConfigure(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE);    }

	public HAPResourceIdUIAppConfigure(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIAppConfigure(String id){
		this();
		init(id, null);
	}

	@Override
	public HAPResourceIdUIAppConfigure clone(){
		HAPResourceIdUIAppConfigure out = new HAPResourceIdUIAppConfigure();
		out.cloneFrom(this);
		return out;
	}
}
