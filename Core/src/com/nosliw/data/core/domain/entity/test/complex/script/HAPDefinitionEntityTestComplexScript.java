package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityTestComplexScript extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT;

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public static final String ATTR_SCRIPT = "script";

	public HAPDefinitionEntityTestComplexScript() {
		super(ENTITY_TYPE);
		this.setNormalAttributeSimple(ATTR_PARM, new HAPEmbededDefinition(new LinkedHashMap<String, Object>()));
	}

	public void setScriptName(String scriptName) {    this.setNormalAttributeSimple(ATTR_SCRIPTNAME, new HAPEmbededDefinition(scriptName));    }
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
