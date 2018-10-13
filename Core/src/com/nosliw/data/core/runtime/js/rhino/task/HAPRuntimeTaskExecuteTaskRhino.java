package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteTask;
import com.nosliw.data.core.task.HAPExecutableTask;

public class HAPRuntimeTaskExecuteTaskRhino extends HAPRuntimeTaskExecuteTask{

	public HAPRuntimeTaskExecuteTaskRhino(HAPExecutableTask task, Map<String, Object> input) {
		super(task, input);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		return null;
	}

}
