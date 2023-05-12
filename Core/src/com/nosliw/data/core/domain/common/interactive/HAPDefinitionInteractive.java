package com.nosliw.data.core.domain.common.interactive;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPDefinitionInteractive extends HAPSerializable{

	@HAPAttribute
	public static String REQUEST = "request";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	List<HAPDefinitionInteractiveRequestParm> getRequestParms();
	
	Map<String, HAPDefinitionInteractiveResult> getResults();

	HAPDefinitionInteractive cloneWithInteractive();
}
