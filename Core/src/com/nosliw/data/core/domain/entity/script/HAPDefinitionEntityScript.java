package com.nosliw.data.core.domain.entity.script;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;

public class HAPDefinitionEntityScript extends HAPManualBrickSimple{

	private String m_script;
	
	public HAPDefinitionEntityScript() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScript out = new HAPDefinitionEntityScript();
		out.setScript(this.getScript());
		return out;
	}
}
