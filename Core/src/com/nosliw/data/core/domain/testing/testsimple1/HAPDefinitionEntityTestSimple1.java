package com.nosliw.data.core.domain.testing.testsimple1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityTestSimple1 extends HAPDefinitionEntityInDomainSimple{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1;

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public static final String ATTR_SCRIPT = "script";

	private String m_scriptName;

	private Map<String, Object> m_parms;
	
	public HAPDefinitionEntityTestSimple1() {
		super(ENTITY_TYPE);
		this.m_parms = new LinkedHashMap<String, Object>();
	}

	public String getScriptName() {   return this.m_scriptName;     }
	public void setScriptName(String scriptName) {    this.m_scriptName = scriptName;    }
	
	public void setParm(String name, Object value) {    this.m_parms.put(name, value);     }
	public Object getParm(String name) {   return this.m_parms.get(name);    }
	public Map<String, Object> getParms(){   return this.m_parms;    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestSimple1 out = new HAPDefinitionEntityTestSimple1();
		out.cloneToDefinitionEntityInDomain(out);
		out.setScriptName(this.getScriptName());
		for(String parmName : this.m_parms.keySet())    out.setParm(parmName, this.m_parms.get(parmName));
		return out;
	}
	
}
