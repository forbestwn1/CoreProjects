package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPRuntimeProcess {

	public void executeProcess(HAPExecutableProcess processExe, Map<String, HAPData> input, HAPProcessResultHandler resultHandler);
	
}
