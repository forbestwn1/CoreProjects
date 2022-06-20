package com.nosliw.data.core.complex;

import com.nosliw.data.core.runtime.HAPExecutableImp;

public interface HAPPluginAdapterProcessor {

	String getAdapterType();

	Object process(Object adapter, HAPExecutableImp parentEntityExecutable, HAPExecutableImp entityExecutable);
	
}
