package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * Data type id to specify the data type
 * 		name : the name of data type
 * 		version: the same data type may have different version
 * Therefore, name and version together are related with a unique data type
 */
@HAPEntityWithAttribute(baseName="DATATYPEID")
public interface HAPDataTypeId extends HAPSerializable{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String VERSION = "version";

	String getName();
	
	HAPDataTypeVersion getVersion();
	
}
