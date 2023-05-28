package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutable;

public interface HAPPluginAdapterProcessor {

	String getAdapterType();

	Object process(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext);
	
}
