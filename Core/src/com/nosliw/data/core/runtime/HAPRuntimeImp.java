package com.nosliw.data.core.runtime;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;

public abstract class HAPRuntimeImp implements HAPRuntime{

	public HAPRuntimeImp(){
		HAPResourceHelper resourceHelper = HAPResourceHelper.getInstance();
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, HAPResourceIdOperation.class, HAPOperationId.class);
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, HAPResourceIdConverter.class, HAPDataTypeConverter.class);
	}
	
}
