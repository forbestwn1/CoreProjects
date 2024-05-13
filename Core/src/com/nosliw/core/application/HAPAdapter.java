package com.nosliw.core.application;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPAdapter extends HAPExecutableImpEntityInfo implements HAPWithResourceDependency{

	private HAPWrapperValue m_valueWrapper;
	
	public HAPAdapter(HAPWrapperValue valueWrapper) {
		this.m_valueWrapper = valueWrapper;
	}
	
	public HAPWrapperValue getValueWrapper() {
		return this.m_valueWrapper;
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
	}
}
