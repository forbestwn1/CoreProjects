package com.nosliw.data.core.process1;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.structure.data.HAPContextData;

public interface HAPRuntimeProcess {

	void executeProcess(HAPExecutableProcess processExe, HAPContextData input, HAPProcessResultHandler resultHandler, HAPResourceManagerRoot resourceManager);

	void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe, HAPContextData parentContext, HAPProcessResultHandler resultHandler, HAPResourceManagerRoot resourceManager);

	HAPServiceData executeProcess(HAPExecutableProcess process, HAPContextData parentContext, HAPResourceManagerRoot resourceManager);
 
	HAPServiceData executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData input, HAPResourceManagerRoot resourceManager);
	
}
