package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBrickTestComplexScript extends HAPManualDefinitionBrickBlockComplex{

	public HAPManualDefinitionBrickTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
		this.setAttributeWithValueValue(HAPBlockTestComplexScript.PARM, new LinkedHashMap<String, Object>());
	}

	public void setScript(HAPResourceId scriptResourceId) {    this.setAttributeWithValueValue(HAPBlockTestComplexScript.SCRIPT, scriptResourceId);    }
	public HAPResourceId getScript() {   return (HAPResourceId)this.getAttributeValueWithValue(HAPBlockTestComplexScript.SCRIPT);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueWithValue(HAPBlockTestComplexScript.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
}
