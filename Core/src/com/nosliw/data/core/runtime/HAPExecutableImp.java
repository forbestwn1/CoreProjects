package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPExecutableImp  extends HAPSerializableImp implements HAPExecutable{

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return new ArrayList<HAPResourceDependent>();
	}

}
