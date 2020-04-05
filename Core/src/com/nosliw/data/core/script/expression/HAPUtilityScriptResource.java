package com.nosliw.data.core.script.expression;

import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityScriptResource {

	public static HAPResourceId buildResourceId(String id) {
		return new HAPResourceIdScriptGroup(new HAPScriptGroupId(id));
	}
}
