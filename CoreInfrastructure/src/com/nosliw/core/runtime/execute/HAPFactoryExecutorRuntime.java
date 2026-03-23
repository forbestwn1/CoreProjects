package com.nosliw.core.runtime.execute;

import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPFactoryExecutorRuntime {

	HAPRuntimeInfo getRuntimeInfo();
	
	HAPExecutorRuntime newRuntimeExecutor();

}
