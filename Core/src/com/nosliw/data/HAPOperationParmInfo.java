package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATAOPERATIONPARMINFO")
public interface HAPOperationParmInfo {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATATYPECRITERIA = "dataTypeCriteria";

	String getName();
	
	String getDescription();
	
	HAPDataTypeCriteria getDataTypeCriteria();

}
