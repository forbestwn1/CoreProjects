package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.brick.interactive.interfacee.expression.HAPBlockInteractiveInterfaceExpression;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;

public class HAPManualBlockTestComplexTask extends HAPManualBrickImp implements HAPBlockTestComplexTask{

	public HAPManualBlockTestComplexTask() {
	}

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VARIABLE, new LinkedHashMap<String, HAPResultReferenceResolve>());
	}

	@Override
	public Map<String, Object> getParms() {		return (Map<String, Object>)this.getAttributeValueOfValue(PARM);	}
	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	@Override
	public Map<String, HAPIdElement> getVariables() {    return (Map<String, HAPIdElement>)this.getAttributeValueOfValue(VARIABLE);  }

	@Override
	public HAPBlockInteractiveInterfaceTask getTaskInteractive() {   return (HAPBlockInteractiveInterfaceTask)this.getAttributeValueOfBrickLocal(INTERACTIVETASK);  }

	@Override
	public String getTaskInteractiveResult() {   return (String)this.getAttributeValueOfValue(INTERACTIVETASKRESULT);  }
	public void setTaskInteractiveResult(String result) {   this.setAttributeValueWithValue(HAPBlockTestComplexTask.INTERACTIVETASKRESULT, result);  }

	@Override
	public HAPBlockInteractiveInterfaceExpression getExpressionInteractive() {   return (HAPBlockInteractiveInterfaceExpression)this.getAttributeValueOfBrickLocal(INTERACTIVEEXPRESSION);  }
	
}
 