package com.nosliw.data.core.resource;

import java.util.List;

import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public interface HAPResourceManager {

	
	/**
	 * Response including loaded resoures and fail resources, the sequence should be the same as input. Sequence matters
	 * @param resources
	 * @return a list of resource. here, the position matter as some resources has to be load first
	 */
	HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo);

	HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo);

	/**
	 * Discover resource information (dependency)
	 * @param resource info
	 * @return
	 */
	HAPResourceInfo discoverResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo);

}
