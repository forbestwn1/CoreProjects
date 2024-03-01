package com.nosliw.data.core.domain.entity.configure;

import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;

public class HAPDefinitionEntityConfigure extends HAPManualEntitySimple{

	private String m_script;
	
	public HAPDefinitionEntityConfigure() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityConfigure out = new HAPDefinitionEntityConfigure();
		out.setScript(this.getScript());
		return out;
	}
}
