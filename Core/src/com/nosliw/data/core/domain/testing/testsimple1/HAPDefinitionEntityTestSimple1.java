package com.nosliw.data.core.domain.testing.testsimple1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.HAPEmbededDefinitionWithValue;

public class HAPDefinitionEntityTestSimple1 extends HAPDefinitionEntityInDomainSimple{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1;

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public static final String ATTR_SCRIPT = "script";

	public HAPDefinitionEntityTestSimple1() {
		super(ENTITY_TYPE);
		this.setSimpleAttribute(ATTR_SCRIPTNAME, new HAPEmbededDefinitionWithValue(new LinkedHashMap<String, Object>()));
	}

	public void setScriptName(String scriptName) {    this.setSimpleAttribute(ATTR_SCRIPTNAME, new HAPEmbededDefinitionWithValue(scriptName));    }
	public String getScriptName() {   return (String)this.getSimpleAttributeWithValue(ATTR_SCRIPTNAME).getValue().getValue();     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getSimpleAttributeWithValue(ATTR_PARM).getValue();    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestSimple1 out = new HAPDefinitionEntityTestSimple1();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
