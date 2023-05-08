package com.nosliw.data.core.domain.entity.scriptbased;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityScriptComplex extends HAPDefinitionEntityInDomainComplex{

	private String m_script;
	
	public HAPDefinitionEntityScriptComplex() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScriptComplex out = new HAPDefinitionEntityScriptComplex();
		out.setScript(this.getScript());
		return out;
	}
}
