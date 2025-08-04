package com.nosliw.core.application.division.manual.core;

import java.util.List;

import com.nosliw.core.application.HAPAdapter;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPManualAdapter extends HAPAdapter{

	private HAPWrapperValue m_valueWrapper;
	
	public HAPManualAdapter(HAPWrapperValue valueWrapper) {
		this.m_valueWrapper = valueWrapper;
	}
	
	@Override
	public HAPWrapperValue getValueWrapper() {
		return this.m_valueWrapper;
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
	}
}
