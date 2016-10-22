package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute(baseName="DATATYPEINFO")
public interface HAPDataTypeInfo extends HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String CATEGARY = "categary";

	@HAPAttribute
	public static String VERSION = "version";

	String getType();
	
	String getCategary();
	
	HAPDataTypeVersion getVersion();
	
}
