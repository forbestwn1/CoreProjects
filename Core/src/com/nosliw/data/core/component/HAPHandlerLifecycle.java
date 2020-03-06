package com.nosliw.data.core.component;

import com.nosliw.data.core.process.HAPEmbededProcessTaskImp;

public class HAPHandlerLifecycle extends HAPEmbededProcessTaskImp{

	public HAPHandlerLifecycle cloneLifecycleHander() {
		HAPHandlerLifecycle out = new HAPHandlerLifecycle();
		this.cloneToEmbededProcessTask(out);
		return out;
	}
	
}
