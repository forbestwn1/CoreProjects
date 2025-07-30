package com.nosliw.core.resource;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPResourceDataOrWrapper extends HAPSerializable{

	HAPResourceDataOrWrapper getDescendant(HAPPath path);

	List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo);
}
