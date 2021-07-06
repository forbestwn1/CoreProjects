package com.nosliw.data.core.task.resource;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.task.HAPIdTaskSuite;

public class HAPUtilityResourceActivity {

	public static HAPResourceId buildResourceId(String suiteId) {
		return new HAPResourceIdActivitySuite(new HAPIdTaskSuite(suiteId));
	}

	
	
}
