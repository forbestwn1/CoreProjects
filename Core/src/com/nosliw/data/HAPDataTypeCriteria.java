package com.nosliw.data;

import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATATYPECRITERIA")
public interface HAPDataTypeCriteria{

	public boolean match(HAPDataTypeInfo dataTypeInfo);

	public boolean match(HAPDataTypeCriteria criteria);
	
}
