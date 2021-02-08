package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIModuleDecoration  extends HAPResourceIdSimple{

	public HAPResourceIdUIModuleDecoration(){   super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION);     }

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
