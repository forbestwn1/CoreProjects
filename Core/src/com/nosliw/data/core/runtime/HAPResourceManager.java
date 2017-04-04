package com.nosliw.data.core.runtime;

import java.util.Set;

public interface HAPResourceManager {

	/**
	 * Prepare the actual resource
	 * @param resources
	 * @return
	 */
	public Set<HAPResource> getResources(Set<HAPResourceId> resourcesId);

}
