package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public interface HAPPluginAdapterProcessor {

	String getAdapterType();

	Object process(Object adapter, HAPExecutableImp parentEntityExecutable, HAPContextProcessor parentContext, HAPExecutableImp entityExecutable, HAPContextProcessor childContext);
	
}
