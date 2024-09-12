package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.resource.HAPResourceId;

public interface HAPDefinitionEntityScriptBased {

	void setScript(String script);
	String getScript();
	
	void setScriptResourceId(HAPResourceId resourceId);
	HAPResourceId getScriptResourceId();
	
}
