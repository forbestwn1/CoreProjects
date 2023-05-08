package com.nosliw.data.core.domain.entity.scriptbased;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityScript extends HAPDefinitionEntityInDomain{

	private String m_script;
	
	public HAPDefinitionEntityScript() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScript out = new HAPDefinitionEntityScript();
		out.setScript(this.getScript());
		return out;
	}
}
