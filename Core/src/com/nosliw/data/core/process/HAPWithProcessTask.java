package com.nosliw.data.core.process;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public interface HAPWithProcessTask {

	@HAPAttribute
	public static String PROCESS = "process";
	
	HAPDefinitionWrapperTask<String> getProcess();
	
	void setProcess(HAPDefinitionWrapperTask<String> processTask);

}
