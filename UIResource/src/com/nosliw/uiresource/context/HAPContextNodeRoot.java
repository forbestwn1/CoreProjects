package com.nosliw.uiresource.context;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPContextNodeRoot extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type";
	
	String getType();
	
}
