package com.nosliw.data.core.component;

import com.nosliw.data.core.process.HAPEmbededProcessTaskImp;

public class HAPHandlerEvent extends HAPEmbededProcessTaskImp{

	public HAPHandlerEvent cloneEventHandler() {
		HAPHandlerEvent out = new HAPHandlerEvent();
		this.cloneToEmbededProcessTask(out);
		return out;
	}
	
	
}
