package com.nosliw.data.core.domain.entity.decoration.script1;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityDecorationScript extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_SCRIPT = "script";
	
	public HAPDefinitionEntityDecorationScript() {
	}
	
	public String getScriptName() {   return (String)this.getNormalAttributeValue(ATTR_SCRIPTNAME);     }
	public void setScriptName(String scriptName) {    this.setNormalAttributeObject(ATTR_SCRIPTNAME, new HAPEmbededDefinition(scriptName));    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDecorationScript out = new HAPDefinitionEntityDecorationScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
