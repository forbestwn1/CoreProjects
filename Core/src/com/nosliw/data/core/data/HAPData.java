package com.nosliw.data.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * class that store data which is composed of two part: value and dataTypeInfo 
 *  
 */
@HAPEntityWithAttribute(baseName="DATA")
public interface HAPData extends HAPSerializable{
	
	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";

	@HAPAttribute
	public static String VALUE = "value";

	@HAPAttribute
	public static String INFO = "info";
	
	/**
	 * get data type object
	 */
	HAPDataTypeId getDataTypeId();
	
	/**
	 * get value within data
	 */
	Object getValue();
	
	// extra info related with data
	HAPInfo getInfo();
	
	HAPData cloneData();
}
