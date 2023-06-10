package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.resource.HAPResourceId;

public interface HAPDefinitionEntityScript {

	public static final String ATTR_SCRIPT = "script";
	
	public static final String ATTR_RESOURCEID = "resourceId";
	
	void setScript(String script);
	String getScript();
	
	void setScriptResourceId(HAPResourceId resourceId);
	HAPResourceId getScriptResourceId();
	
}
