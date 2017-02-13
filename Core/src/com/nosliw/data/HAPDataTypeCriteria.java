package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * DataTypeCriteria is a way to describe what data type is possible
 * It can be used when 
 * 		define operation parm --- what possible data type is acceptable as parm
 * 		define operation output --- what possible data type will created from operation
 */
@HAPEntityWithAttribute(baseName="DATATYPECRITERIA")
public interface HAPDataTypeCriteria{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	String getCriteria();
	
//	public boolean match(HAPDataTypeInfo dataTypeInfo);
//	public boolean match(HAPDataTypeCriteria criteria);
	
}
