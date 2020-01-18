package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public interface HAPRuntimeProcess {

	public void executeProcess(HAPExecutableProcess processExe, Map<String, HAPData> input, HAPProcessResultHandler resultHandler);

	public void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe, Map<String, HAPData> input, HAPProcessResultHandler resultHandler);

}
