package com.nosliw.data.core.resource;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public interface HAPResourceDataOrWrapper extends HAPSerializable{

	HAPResourceDataOrWrapper getDescendant(HAPPath path);

	List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo);
}
