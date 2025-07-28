package com.nosliw.core.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPWrapperResourceDataImp extends HAPSerializableImp implements HAPWrapperResourceData{

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		throw new RuntimeException();
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return new ArrayList<HAPResourceDependency>();
	}

}
