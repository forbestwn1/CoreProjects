package com.nosliw.core.application.common.script;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.core.application.division.manual.executable.HAPBrickBlockComplex;

public class HAPBlockScriptComplex extends HAPBrickBlockComplex implements HAPWithScript{

	
	public void setScript(String script) {		this.setAttributeValueWithValue(SCRIPT, new HAPJsonTypeScript(script));	}
	
}
