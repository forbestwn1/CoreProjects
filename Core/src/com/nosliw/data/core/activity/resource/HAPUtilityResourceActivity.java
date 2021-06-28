package com.nosliw.data.core.activity.resource;

import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityResourceActivity {

	public static HAPResourceId buildResourceId(String suiteId) {
		return new HAPResourceIdActivitySuite(new HAPIdActivitySuite(suiteId));
	}

	
	
}
