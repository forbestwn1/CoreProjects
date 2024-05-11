package com.nosliw.data.core.resource;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public interface HAPResourceDataOrWrapper {

	HAPResourceDataOrWrapper getDescendant(HAPPath path);

	List<HAPResourceDependency> getResourceDependency();
	
}
