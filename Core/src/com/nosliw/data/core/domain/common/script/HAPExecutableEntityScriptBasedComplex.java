package com.nosliw.data.core.domain.common.script;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPExecutableEntityScriptBasedComplex extends HAPExecutableEntityComplex implements HAPExecutableEntityScriptBased{

	public HAPExecutableEntityScriptBasedComplex() {	}

	@Override
	public void setScript(String script) {		this.setAttributeValueObject(SCRIPT, new HAPJsonTypeScript(script));	}

	@Override
	public void setResrouceId(HAPResourceId resourceId) {   this.setAttributeValueObject(SCRIPT, resourceId);	}

}
