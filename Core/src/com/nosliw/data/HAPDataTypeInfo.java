package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * Data type info to specify the data type
 * 		name : the name of data type
 * 		version: the same data type may have different version
 * Therefore, name and version together are related with a unique data type
 */
@HAPEntityWithAttribute(baseName="DATATYPEINFO")
public interface HAPDataTypeInfo extends HAPSerializable{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String VERSION = "version";

	String getName();
	
	HAPDataTypeVersion getVersion();
	
}
