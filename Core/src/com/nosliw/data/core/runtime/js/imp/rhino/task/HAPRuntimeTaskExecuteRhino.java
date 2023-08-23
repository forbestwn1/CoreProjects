package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.Map;

import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteTask;

public class HAPRuntimeTaskExecuteRhino extends HAPRuntimeTaskExecuteTask{

	public HAPRuntimeTaskExecuteRhino(HAPExecutableProcess task, Map<String, Object> input) {
		super(task, input);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		return null;
	}

}
