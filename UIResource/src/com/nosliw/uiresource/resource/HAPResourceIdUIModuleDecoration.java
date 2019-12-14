package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIModuleDecoration  extends HAPResourceId{

	public HAPResourceIdUIModuleDecoration(){}

	public HAPResourceIdUIModuleDecoration(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIModuleDecoration(String id){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION, id, null);
	}

	
	@Override
	public HAPResourceIdUIModuleDecoration clone(){
		HAPResourceIdUIModuleDecoration out = new HAPResourceIdUIModuleDecoration();
		out.cloneFrom(this);
		return out;
	}

}
