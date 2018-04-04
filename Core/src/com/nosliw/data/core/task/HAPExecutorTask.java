package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPExecutorTask {

	HAPData execute(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache, HAPLog logger);
	
}
