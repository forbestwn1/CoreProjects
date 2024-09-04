package com.nosliw.core.application.brick.test.complex.task;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.data.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public interface HAPBlockTestComplexTask extends HAPBrick{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String VARIABLE = "variable";

	HAPResourceId getScrip();
	Map<String, Object> getParms();
	Map<String, HAPResultReferenceResolve> getVariables();
	
}
