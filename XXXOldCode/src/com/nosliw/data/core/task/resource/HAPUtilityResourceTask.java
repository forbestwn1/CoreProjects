package com.nosliw.data.core.task.resource;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.task.HAPIdTaskSuite;

public class HAPUtilityResourceTask {

	public static HAPResourceId buildResourceId(String suiteId) {
		return new HAPResourceIdTaskSuite(new HAPIdTaskSuite(suiteId));
	}

	
	
}
