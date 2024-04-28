package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBlockComplex;

public class HAPManualBrickTestComplexScript extends HAPManualBlockComplex{

	public static final String ATTR_SCRIPTNAME = "scriptName";
	public static final String ATTR_PARM = "parm";

	public HAPManualBrickTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
		this.setAttributeWithValueValue(ATTR_PARM, new LinkedHashMap<String, Object>());
	}

	public void setScriptName(String scriptName) {    this.setAttributeWithValueValue(ATTR_SCRIPTNAME, scriptName);    }
	public String getScriptName() {   return (String)this.getAttributeValueWithValue(ATTR_SCRIPTNAME);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueWithValue(ATTR_PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
	@Override
	public String getEntityOrReferenceType() {
		// TODO Auto-generated method stub
		return null;
	}
}
