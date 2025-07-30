package com.nosliw.core.runtime;

import com.nosliw.common.script.HAPJSScriptInfo;

public interface HAPRuntimeWithScript extends HAPRuntime{

	void loadScript(HAPJSScriptInfo scriptInfo) throws Exception;
	
}
