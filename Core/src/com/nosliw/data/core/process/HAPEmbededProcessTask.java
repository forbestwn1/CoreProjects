package com.nosliw.data.core.process;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public interface HAPEmbededProcessTask {

	@HAPAttribute
	public static String PROCESS = "process";
	
	HAPDefinitionWrapperTask<String> getTask();
	
	void setTask(HAPDefinitionWrapperTask<String> processTask);

	String getProcess();
	
}
