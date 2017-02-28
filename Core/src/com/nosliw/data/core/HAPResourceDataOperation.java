package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="RESOURCEDATAOPERATION")
public interface HAPResourceDataOperation extends HAPResource{

	@HAPAttribute
	public static String DATATYPECRITERIA = "dataTypeCriteria";
	
	@HAPAttribute
	public static String OPERATION = "operation";
	
	HAPDataTypeCriteria getDataTypeCriteria();
	
	String getOperation();
	
}
