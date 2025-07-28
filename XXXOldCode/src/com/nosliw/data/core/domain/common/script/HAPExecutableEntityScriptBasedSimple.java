package com.nosliw.data.core.domain.common.script;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPExecutableEntityScriptBasedSimple extends HAPExecutableEntity implements HAPExecutableEntityScriptBased{

	public HAPExecutableEntityScriptBasedSimple() {	}

	@Override
	public void setScript(String script) {		this.setAttributeValueObject(SCRIPT, new HAPJsonTypeScript(script));	}
	
	@Override
	public void setResrouceId(HAPResourceId resourceId) {   this.setAttributeValueObject(SCRIPT, resourceId);	}

}
