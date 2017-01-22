package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * class that store data which is composed of two part: value and dataTypeInfo 
 *  
 */
@HAPEntityWithAttribute(baseName="DATA")
public interface HAPData extends HAPSerializable{
	
	@HAPAttribute
	public static String VALUE = "value";

	@HAPAttribute
	public static String DATATYPEINFO = "dataTypeInfo";
	
	/**
	 * get data type object
	 */
	public HAPDataTypeInfo getDataTypeInfo();
	
	/**
	 * get value within data
	 */
	public Object getValue();
	
}
