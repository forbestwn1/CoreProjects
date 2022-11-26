package com.nosliw.data.core.domain.entity.test.decoration.testdecoration1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithValue;

public class HAPDefinitionEntityTestDecoration1 extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_DECORATION_1;

	public static final String ATTR_SCRIPT = "script";
	
	public HAPDefinitionEntityTestDecoration1() {
	}
	
	public String getScript() {   return (String)this.getNormalAttributeWithValue(ATTR_SCRIPT).getValue().getValue();   }
	public void setScript(String script) {
		this.setNormalAttribute(ATTR_SCRIPT, new HAPEmbededDefinitionWithValue(script));		
	}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestDecoration1 out = new HAPDefinitionEntityTestDecoration1();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
