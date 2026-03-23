package com.nosliw.core.runtime.execute;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.script.HAPJSScriptInfo;

@HAPEntityWithAttribute
public interface HAPExecutorRuntimeWithScript extends HAPExecutorRuntime{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	void loadScript(HAPJSScriptInfo scriptInfo) throws Exception;
	
}
