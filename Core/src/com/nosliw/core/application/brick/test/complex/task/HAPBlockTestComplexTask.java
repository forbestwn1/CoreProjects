package com.nosliw.core.application.brick.test.complex.task;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.data.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public interface HAPBlockTestComplexTask extends HAPBrick{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";

	HAPResourceId getScrip();
	Map<String, Object> getParms();
	List<HAPTestEvent> getEvents();

}
