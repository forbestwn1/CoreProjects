package com.nosliw.data.core.domain.entity.configure;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityConfigure extends HAPDefinitionEntityInDomainSimple{

	private String m_script;
	
	public HAPDefinitionEntityConfigure() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityConfigure out = new HAPDefinitionEntityConfigure();
		out.setScript(this.getScript());
		return out;
	}
}
