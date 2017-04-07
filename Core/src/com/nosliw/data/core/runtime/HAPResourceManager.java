package com.nosliw.data.core.runtime;

import java.util.List;

public interface HAPResourceManager {

	/**
	 * Prepare the actual resource
	 * @param resources
	 * @return
	 */
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId);

}
