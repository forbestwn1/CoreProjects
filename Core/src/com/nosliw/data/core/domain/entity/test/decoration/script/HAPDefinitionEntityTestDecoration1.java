package com.nosliw.data.core.domain.entity.test.decoration.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityTestDecoration1 extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_DECORATION_SCRIPT;

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_SCRIPT = "script";
	
	public HAPDefinitionEntityTestDecoration1() {
	}
	
	public String getScriptName() {   return (String)this.getNormalAttributeValue(ATTR_SCRIPTNAME);     }
	public void setScriptName(String scriptName) {    this.setNormalAttributeObject(ATTR_SCRIPTNAME, new HAPEmbededDefinition(scriptName));    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestDecoration1 out = new HAPDefinitionEntityTestDecoration1();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
