package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * Root node in context definition 
 */
@HAPEntityWithAttribute
public interface HAPContextNodeRoot extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String INFO = "info";
	
	String getType();
	
	HAPContextNodeRootInfo getInfo();
	
	HAPContextNodeRoot toSolidContextNodeRoot(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv);

	HAPContextNodeRoot cloneContextNodeRoot();
}
