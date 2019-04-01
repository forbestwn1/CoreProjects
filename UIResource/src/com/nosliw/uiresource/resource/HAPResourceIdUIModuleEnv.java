package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIModuleEnv  extends HAPResourceId{

	public HAPResourceIdUIModuleEnv(){}

	public HAPResourceIdUIModuleEnv(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIModuleEnv(String id){
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULEENV, id);
	}

	
	@Override
	public HAPResourceIdUIModuleEnv clone(){
		HAPResourceIdUIModuleEnv out = new HAPResourceIdUIModuleEnv();
		out.cloneFrom(this);
		return out;
	}

}
