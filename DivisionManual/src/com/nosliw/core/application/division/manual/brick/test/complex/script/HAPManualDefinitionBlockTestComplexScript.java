package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPTestTaskTrigguer;

public class HAPManualDefinitionBlockTestComplexScript extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
		this.setAttributeValueWithValue(HAPBlockTestComplexScript.PARM, new LinkedHashMap<String, Object>());
		this.setAttributeValueWithValue(HAPBlockTestComplexScript.TASKTRIGGUER, new ArrayList<HAPTestTaskTrigguer>());
	}

	public void setScript(HAPResourceId scriptResourceId) {    this.setAttributeValueWithValue(HAPBlockTestComplexScript.SCRIPT, scriptResourceId);    }
	public HAPResourceId getScript() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTestComplexScript.SCRIPT);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueOfValue(HAPBlockTestComplexScript.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }

	public List<HAPTestTaskTrigguer> getTaskTrigguers(){   return (List<HAPTestTaskTrigguer>)this.getAttributeValueOfValue(HAPBlockTestComplexScript.TASKTRIGGUER);    }
}
