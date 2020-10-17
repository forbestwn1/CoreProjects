package com.nosliw.data.core.dataable;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPData;

public class HAPManagerDataable {

	public HAPExecutableDataable process(HAPDefinitionDataable definition) {
		String type = definition.getDataableType();
		if(type.equals(HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION)) {
			
		}
		
		return null;
	}
	
	public HAPData execute() {
		return null;
	}
}
