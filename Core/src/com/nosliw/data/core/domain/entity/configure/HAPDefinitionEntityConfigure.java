package com.nosliw.data.core.domain.entity.configure;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;

public class HAPDefinitionEntityConfigure extends HAPManualBrickSimple{

	private String m_script;
	
	public HAPDefinitionEntityConfigure() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityConfigure out = new HAPDefinitionEntityConfigure();
		out.setScript(this.getScript());
		return out;
	}
}
