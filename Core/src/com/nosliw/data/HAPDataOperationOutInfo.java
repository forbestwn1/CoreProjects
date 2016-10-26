package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATAOPERATIONOUTPUTINFO")
public interface HAPDataOperationOutInfo {

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATATYPE = "dataType";

	String getDescription();	
	
	HAPDataTypeInfo getDataTypeInfo();
	
}
