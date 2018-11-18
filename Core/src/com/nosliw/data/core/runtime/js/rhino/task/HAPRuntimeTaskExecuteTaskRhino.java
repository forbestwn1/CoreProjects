package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.Map;

import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteTask;

public class HAPRuntimeTaskExecuteTaskRhino extends HAPRuntimeTaskExecuteTask{

	public HAPRuntimeTaskExecuteTaskRhino(HAPExecutableProcess task, Map<String, Object> input) {
		super(task, input);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		return null;
	}

}
