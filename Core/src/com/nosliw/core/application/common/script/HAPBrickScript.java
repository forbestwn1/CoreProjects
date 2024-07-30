package com.nosliw.core.application.common.script;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

public class HAPBrickScript extends HAPBrickImp implements HAPWithScript{

	public HAPBrickScript(HAPIdBrickType brickTypeId) {
		this.setBrickType(brickTypeId);
	}
	
	public void setScript(String script) {
		this.setAttribute(new HAPAttributeInBrick(SCRIPT, new HAPWrapperValueOfValue(new HAPJsonTypeScript(script))));
	}

	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		// TODO Auto-generated method stub
		return null;
	}


}
