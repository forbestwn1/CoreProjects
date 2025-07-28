package com.nosliw.core.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPResourceDataImp extends HAPSerializableImp implements HAPResourceData, HAPWithResourceDependency{

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		throw new RuntimeException();
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		this.buildResourceDependency(out, runtimeInfo);
		return out;
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {	}
}
