package com.nosliw.data.core.process1;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.application.common.structure.data.HAPContextData;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.resource.HAPResourceManager;

public interface HAPRuntimeProcess {

	void executeProcess(HAPExecutableProcess processExe, HAPContextData input, HAPProcessResultHandler resultHandler, HAPResourceManager resourceManager);

	void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe, HAPContextData parentContext, HAPProcessResultHandler resultHandler, HAPResourceManager resourceManager);

	HAPServiceData executeProcess(HAPExecutableProcess process, HAPContextData parentContext, HAPResourceManager resourceManager);
 
	HAPServiceData executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData input, HAPResourceManager resourceManager);
	
}
