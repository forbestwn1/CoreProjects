package com.nosliw.data.core.runtime.js;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdHelper extends HAPResourceId{

	public HAPResourceIdHelper(){}

	public HAPResourceIdHelper(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdHelper(String id, String alias) {
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_HELPER, id, alias);
	}

	public HAPResourceIdHelper clone(){
		HAPResourceIdHelper out = new HAPResourceIdHelper();
		out.cloneFrom(this);
		return out;
	}

}
