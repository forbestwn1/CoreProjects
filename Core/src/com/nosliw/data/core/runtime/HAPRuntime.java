package com.nosliw.data.core.runtime;

public interface HAPRuntime {

	public void executeTask(HAPRuntimeTask task);

	void close();
	
	void start();

}
