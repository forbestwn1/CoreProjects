package com.nosliw.data.core.runtime;

import java.util.List;

public interface HAPResourceManager {

	/**
	 * Prepare the actual resource
	 * @param resources
	 * @return a list of resource. here, the position matter as some resources has to be load first
	 */
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId);

}
