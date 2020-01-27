package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIModuleDecoration  extends HAPResourceIdSimple{

	public HAPResourceIdUIModuleDecoration(){   super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION);     }

	public HAPResourceIdUIModuleDecoration(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIModuleDecoration(String id){
		this();
		init(id, null);
	}
	
	@Override
	public HAPResourceIdUIModuleDecoration clone(){
		HAPResourceIdUIModuleDecoration out = new HAPResourceIdUIModuleDecoration();
		out.cloneFrom(this);
		return out;
	}

}
