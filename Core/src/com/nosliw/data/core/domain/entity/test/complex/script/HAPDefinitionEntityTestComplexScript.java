package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithValue;

public class HAPDefinitionEntityTestComplexScript extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT;

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public static final String ATTR_SCRIPT = "script";

	public HAPDefinitionEntityTestComplexScript() {
		super(ENTITY_TYPE);
		this.setNormalAttribute(ATTR_PARM, new HAPEmbededDefinitionWithValue(new LinkedHashMap<String, Object>()));
	}

	public void setScriptName(String scriptName) {    this.setNormalAttribute(ATTR_SCRIPTNAME, new HAPEmbededDefinitionWithValue(scriptName));    }
	public String getScriptName() {   return (String)this.getNormalAttributeWithValue(ATTR_SCRIPTNAME).getValue().getValue();     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getNormalAttributeWithValue(ATTR_PARM).getValue().getValue();    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplexScript out = new HAPDefinitionEntityTestComplexScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
