package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTestComplexTask extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTestComplexTask() {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100);
		this.setAttributeValueWithValue(HAPBlockTestComplexTask.PARM, new LinkedHashMap<String, Object>());
	}

	public void setScript(HAPResourceId scriptResourceId) {    this.setAttributeValueWithValue(HAPBlockTestComplexTask.SCRIPT, scriptResourceId);    }
	public HAPResourceId getScript() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTestComplexTask.SCRIPT);     }
	
	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueOfValue(HAPBlockTestComplexTask.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }
}
