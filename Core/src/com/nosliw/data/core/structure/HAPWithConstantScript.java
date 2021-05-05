package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public interface HAPWithConstantScript {

	Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv);
	
}
