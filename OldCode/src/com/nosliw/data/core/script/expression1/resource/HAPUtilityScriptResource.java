package com.nosliw.data.core.script.expression1.resource;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.expression1.HAPIdScriptGroup;

public class HAPUtilityScriptResource {

	public static HAPResourceId buildResourceId(String id) {
		return new HAPResourceIdScriptGroup(new HAPIdScriptGroup(id));
	}
}
