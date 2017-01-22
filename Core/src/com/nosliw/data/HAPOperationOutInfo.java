package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * Entity for operation output information
 * Output data type sometimes can be decided at runtime, so we use HAPDataTypeCriteria for output type 
 */
@HAPEntityWithAttribute(baseName="DATAOPERATIONOUTPUTINFO")
public interface HAPOperationOutInfo {

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATATYPECRITERIA = "dataTypeCriteria";

	String getDescription();	
	
	HAPDataTypeCriteria getDataTypeCriteria();
	
}
