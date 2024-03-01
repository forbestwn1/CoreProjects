package com.nosliw.data.core.domain.entity.script;

import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;

public class HAPDefinitionEntityScript extends HAPManualEntitySimple{

	private String m_script;
	
	public HAPDefinitionEntityScript() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScript out = new HAPDefinitionEntityScript();
		out.setScript(this.getScript());
		return out;
	}
}
