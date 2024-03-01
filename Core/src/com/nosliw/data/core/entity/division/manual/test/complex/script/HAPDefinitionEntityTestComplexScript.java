package com.nosliw.data.core.entity.division.manual.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPDefinitionEntityTestComplexScript extends HAPManualEntityComplex{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public HAPDefinitionEntityTestComplexScript() {
		this.setAttributeValue(ATTR_PARM, new LinkedHashMap<String, Object>());
	}

	public void setScriptName(String scriptName) {    this.setAttributeValue(ATTR_SCRIPTNAME, scriptName);    }
	public String getScriptName() {   return (String)this.getAttributeValue(ATTR_SCRIPTNAME);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValue(ATTR_PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplexScript out = new HAPDefinitionEntityTestComplexScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
