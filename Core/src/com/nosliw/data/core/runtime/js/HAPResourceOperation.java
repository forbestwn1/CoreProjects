package com.nosliw.data.core.runtime.js;

import com.nosliw.data.core.HAPInfo;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceOperation extends HAPResource{

	public HAPResourceOperation(HAPResourceId id, Object resourceData, HAPInfo info) {
		super(id, resourceData, info);
	}

	public String getOperationScript(){
		return (String)this.getResourceData();
	}
	
}
