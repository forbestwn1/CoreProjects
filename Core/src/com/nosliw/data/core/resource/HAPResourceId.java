package com.nosliw.data.core.resource;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPResourceId {

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TYPE = "type";
	
	String getId();
	
	String getType();
	
}
