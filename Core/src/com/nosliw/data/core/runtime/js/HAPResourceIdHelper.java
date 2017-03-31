package com.nosliw.data.core.runtime.js;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdHelper extends HAPResourceId{

	public HAPResourceIdHelper(){}
	
	public HAPResourceIdHelper(String id, String alias) {
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER, id, alias);
	}

	public HAPResourceIdHelper clone(){
		HAPResourceIdHelper out = new HAPResourceIdHelper();
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPResourceIdHelper resourceId){
		super.cloneFrom(resourceId);
	}

}
