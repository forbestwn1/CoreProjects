package com.nosliw.data.core.runtime.js.broswer;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;

public abstract class HAPRuntimeImpJSBroswer extends HAPRuntimeImpJS{

	public HAPRuntimeImpJSBroswer(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		super(resourceDiscovery, resourceMan);
	}

	@Override
	public HAPRuntimeInfo getRuntimeInfo() {		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_BROWSER);	}

	@Override
	public void executeExpressionTask(HAPExecuteExpressionTask result) {
		throw new RuntimeException();
	}
	
	@Override
	public void start() {
		
	}


	@Override
	public void close() {
		
	}

}
