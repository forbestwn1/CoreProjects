package com.nosliw.data.core.domain.testing.testdecoration1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityTestDecoration1 extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_DECORATION1;

	private String m_script;
	
	public HAPDefinitionEntityTestDecoration1() {
	}
	
	public String getScript() {   return this.m_script;   }
	public void setScript(String script) {    this.m_script = script;     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestDecoration1 out = new HAPDefinitionEntityTestDecoration1();
		out.setScript(this.getScript());
		return out;
	}

}
