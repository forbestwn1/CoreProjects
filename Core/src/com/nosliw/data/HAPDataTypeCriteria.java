package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATATYPECRITERIA")
public interface HAPDataTypeCriteria extends HAPDataTypeInfo{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String VERSION = "version";

	public boolean isValidDataType(HAPDataTypeInfo dataTypeInfo);
	
}
