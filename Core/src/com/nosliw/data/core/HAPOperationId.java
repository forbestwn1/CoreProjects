package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPOperationId extends HAPDataTypeId{

	@HAPAttribute
	public static String OPERATION = "operation";
	
	String getOperation();
	
}
