package com.nosliw.data.core.resource;

import java.util.List;

import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public interface HAPResourceManagerRoot{

	/**
	 * Response including loaded resoures and fail resources, the sequence should be the same as input. Sequence matters
	 * @param resources
	 * @return a list of resource. here, the position matter as some resources has to be load first
	 */
	HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo);

	/**
	 * Discover resource information (dependency)
	 * Sequence matters
	 * @param resource info
	 * @return
	 */
	List<HAPResourceInfo> discoverResources(List<HAPResourceId> resourceIds, HAPRuntimeInfo runtimeInfo);
	
	
	/**
	 * Register resource manager for particular type
	 * @param type
	 * @param resourceMan
	 */
	public void registerResourceManager(String type, HAPResourceManager resourceMan);

}
