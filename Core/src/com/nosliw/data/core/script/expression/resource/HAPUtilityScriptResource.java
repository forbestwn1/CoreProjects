package com.nosliw.data.core.script.expression.resource;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.expression.HAPIdScriptGroup;

public class HAPUtilityScriptResource {

	public static HAPResourceId buildResourceId(String id) {
		return new HAPResourceIdScriptGroup(new HAPIdScriptGroup(id));
	}
}
