package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATAOPERATIONPARMINFO")
public interface HAPDataOperationParmInfo {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATATYPE = "dataType";

	String getName();
	
	String getDescription();
	
	HAPDataTypeInfo getDataTypeInfo();

}
