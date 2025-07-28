package com.nosliw.data.core.process1;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.application.common.structure.data.HAPContextData;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.resource.HAPManagerResource;

public interface HAPRuntimeProcess {

	void executeProcess(HAPExecutableProcess processExe, HAPContextData input, HAPProcessResultHandler resultHandler, HAPManagerResource resourceManager);

	void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe, HAPContextData parentContext, HAPProcessResultHandler resultHandler, HAPManagerResource resourceManager);

	HAPServiceData executeProcess(HAPExecutableProcess process, HAPContextData parentContext, HAPManagerResource resourceManager);
 
	HAPServiceData executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData input, HAPManagerResource resourceManager);
	
}
