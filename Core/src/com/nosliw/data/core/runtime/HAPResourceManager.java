package com.nosliw.data.core.runtime;

import java.util.List;

public interface HAPResourceManager {

	/**
	 * Response including loaded resoures and fail resources, the sequence should be the same as input. Sequence matters
	 * @param resources
	 * @return a list of resource. here, the position matter as some resources has to be load first
	 */
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId);

	public HAPResource getResource(HAPResourceId resourceId);
	
}
