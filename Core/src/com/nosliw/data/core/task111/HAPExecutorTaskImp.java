package com.nosliw.data.core.task111;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public abstract class HAPExecutorTaskImp implements HAPExecutorTask{

	public abstract HAPData executeTask(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache, HAPLogTask logger);

	@Override
	public HAPData execute(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache,	HAPLogTask logger) {
		logger.setTaskExecutable(task);
		logger.setParms(parms);
		HAPData result = this.executeTask(task, parms, cache, logger);
		logger.setResult(result);
		return result;
	}

}
