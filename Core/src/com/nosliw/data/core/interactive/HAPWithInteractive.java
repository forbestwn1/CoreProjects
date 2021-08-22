package com.nosliw.data.core.interactive;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.data.variable.HAPVariableInfo;

public interface HAPWithInteractive extends HAPSerializable{

	@HAPAttribute
	public static String REQUEST = "request";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	List<HAPVariableInfo> getRequestParms();
	
	Map<String, HAPResultInteractive> getResults();

	HAPWithInteractive cloneWithInteractive();
}
