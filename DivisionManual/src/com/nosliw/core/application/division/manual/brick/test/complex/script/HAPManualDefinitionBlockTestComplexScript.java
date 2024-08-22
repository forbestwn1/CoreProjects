package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTestComplexScript extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
		this.setAttributeValueWithValue(HAPBlockTestComplexScript.PARM, new LinkedHashMap<String, Object>());
	}

	public void setScript(HAPResourceId scriptResourceId) {    this.setAttributeValueWithValue(HAPBlockTestComplexScript.SCRIPT, scriptResourceId);    }
	public HAPResourceId getScript() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTestComplexScript.SCRIPT);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueOfValue(HAPBlockTestComplexScript.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
	
}
