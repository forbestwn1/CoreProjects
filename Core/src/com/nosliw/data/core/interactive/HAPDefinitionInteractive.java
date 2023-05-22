package com.nosliw.data.core.interactive;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPDefinitionInteractive extends HAPSerializable{

	@HAPAttribute
	public static String REQUEST = "request";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	List<HAPDefinitionInteractiveRequestParm> getRequestParms();
	
	Map<String, HAPDefinitionInteractiveResult> getResults();

	HAPDefinitionInteractive cloneInteractiveDefinition();
}
