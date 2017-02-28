package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * Entity for operation parameter definition
 * Parameter use Criteria as its type, so that parameter can be defined in a very flexible way
 * During runtime, as long as input meet the criteria, it is a valid input for parameter 
 *
 */
@HAPEntityWithAttribute(baseName="DATAOPERATIONPARMINFO")
public interface HAPOperationParmInfo extends HAPOperationOutInfo{

	@HAPAttribute
	public static String NAME = "name";

	String getName();
	
}
