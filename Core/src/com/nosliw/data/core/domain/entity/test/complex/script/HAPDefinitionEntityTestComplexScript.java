package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityTestComplexScript extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public HAPDefinitionEntityTestComplexScript() {
		this.setNormalAttributeObject(ATTR_PARM, new HAPEmbededDefinition(new LinkedHashMap<String, Object>()));
	}

	public void setScriptName(String scriptName) {    this.setNormalAttributeObject(ATTR_SCRIPTNAME, new HAPEmbededDefinition(scriptName));    }
	public String getScriptName() {   return (String)this.getNormalAttributeValue(ATTR_SCRIPTNAME);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getNormalAttributeValue(ATTR_PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplexScript out = new HAPDefinitionEntityTestComplexScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
