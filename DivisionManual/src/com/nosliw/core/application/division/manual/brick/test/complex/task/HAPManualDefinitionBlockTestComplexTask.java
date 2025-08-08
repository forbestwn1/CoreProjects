package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.test.complex.task.HAPBlockTestComplexTask;

public class HAPManualDefinitionBlockTestComplexTask extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTestComplexTask() {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100);
		this.setAttributeValueWithValue(HAPBlockTestComplexTask.PARM, new LinkedHashMap<String, Object>());
		this.setAttributeValueWithValue(HAPBlockTestComplexTask.VARIABLE, new LinkedHashMap<String, HAPReferenceElement>());
	}

	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueOfValue(HAPBlockTestComplexTask.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }

	public Map<String, HAPReferenceElement> getVariables(){    return (Map<String, HAPReferenceElement>)this.getAttributeValueOfValue(HAPBlockTestComplexTask.VARIABLE);     }

	public HAPEntityOrReference getTaskInteractiveInterface() {		return this.getAttributeValueOfBrick(HAPBlockTestComplexTask.INTERACTIVETASK);	}

	public String getTaskInteractiveResult() {   return (String)this.getAttributeValueOfValue(HAPBlockTestComplexTask.INTERACTIVETASKRESULT);  }
	
	public void setTaskInteractiveResult(String result) {   this.setAttributeValueWithValue(HAPBlockTestComplexTask.INTERACTIVETASKRESULT, result);  }
	
	public HAPEntityOrReference getExpressionInteractiveInterface() {		return this.getAttributeValueOfBrick(HAPBlockTestComplexTask.INTERACTIVEEXPRESSION);	}

}
