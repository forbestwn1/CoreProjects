package com.nosliw.data.core.runtime.js;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdConverter extends HAPResourceIdOperation{

	public HAPResourceIdConverter(){}
	
	public HAPResourceIdConverter(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdConverter(String idLiterate, String alias) {
		this.init(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, idLiterate, alias);
	}

	public HAPResourceIdConverter(HAPOperationId operationId, String alias){
		this.init(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, null, alias);
		this.setOperationId(operationId);
	}

	public HAPResourceIdConverter clone(){
		HAPResourceIdConverter out = new HAPResourceIdConverter();
		out.cloneFrom(this);
		return out;
	}

}
