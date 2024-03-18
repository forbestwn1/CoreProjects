package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPDefinitionEntityTestComplexScript extends HAPManualBrickComplex{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public HAPDefinitionEntityTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
		this.setAttributeValue(ATTR_PARM, new LinkedHashMap<String, Object>());
	}

	public void setScriptName(String scriptName) {    this.setAttributeValue(ATTR_SCRIPTNAME, scriptName);    }
	public String getScriptName() {   return (String)this.getAttributeValue(ATTR_SCRIPTNAME);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValue(ATTR_PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplexScript out = new HAPDefinitionEntityTestComplexScript();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}

	@Override
	public String getEntityOrReferenceType() {
		// TODO Auto-generated method stub
		return null;
	}
}
