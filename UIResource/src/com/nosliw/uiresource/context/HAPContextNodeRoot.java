package com.nosliw.uiresource.context;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * Root node in context definition 
 */
@HAPEntityWithAttribute
public abstract class HAPContextNodeRoot extends HAPContextNode{

	@HAPAttribute
	public static final String TYPE = "type";
	
	abstract String getType();
	
}
