package com.nosliw.data.core.domain.entity.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityScript extends HAPDefinitionEntityInDomainSimple{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT;

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
