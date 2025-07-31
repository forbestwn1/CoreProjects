package com.nosliw.core.application.entity.script;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public interface HAPWithScriptReference {

	@HAPAttribute
	public static final String SCRIPTRESOURCEID = "scriptResourceId";

	HAPResourceId getScriptResourceId();
	
}
