package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityScript extends HAPDefinitionEntityInDomain{

	public static final String ATTR_SCRIPT = "script";

	public HAPDefinitionEntityScript() {}

	public void setScript(String script) {    this.setNormalAttributeObject(ATTR_SCRIPT, new HAPEmbededDefinition(script));    }
	public String getScript() {   return (String)this.getNormalAttributeValue(ATTR_SCRIPT);     }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScript out = new HAPDefinitionEntityScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
