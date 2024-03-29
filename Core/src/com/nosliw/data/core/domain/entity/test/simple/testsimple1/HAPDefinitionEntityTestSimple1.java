package com.nosliw.data.core.domain.entity.test.simple.testsimple1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;

public class HAPDefinitionEntityTestSimple1 extends HAPManualBrickSimple{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public static final String ATTR_SCRIPT = "script";

	public HAPDefinitionEntityTestSimple1() {
		this.setNormalAttribute(ATTR_PARM, new HAPEmbededDefinitionWithValue(new LinkedHashMap<String, Object>()));
	}

	public void setScriptName(String scriptName) {    this.setNormalAttribute(ATTR_SCRIPTNAME, new HAPEmbededDefinitionWithValue(scriptName));    }
	public String getScriptName() {   return (String)this.getNormalAttributeWithValue(ATTR_SCRIPTNAME).getValue().getValue();     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getNormalAttributeWithValue(ATTR_PARM).getValue().getValue();    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestSimple1 out = new HAPDefinitionEntityTestSimple1();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
